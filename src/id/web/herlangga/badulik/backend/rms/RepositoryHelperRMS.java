package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.DomainObjectFactory;
import id.web.herlangga.badulik.DomainObjectIDScanner;
import id.web.herlangga.badulik.RepositoryHelper;
import id.web.herlangga.badulik.definition.DataType;
import id.web.herlangga.badulik.definition.Field;
import id.web.herlangga.badulik.definition.FieldValuePair;
import id.web.herlangga.badulik.definition.Structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class RepositoryHelperRMS implements RepositoryHelper {
	private String storageName;
	private Structure domainObjectStructure;
	private DomainObjectIDScanner idScanner;

	public RepositoryHelperRMS(String storageName,
			Structure domainObjectStructure, DomainObjectIDScanner idScanner) {
		this.storageName = storageName;
		this.domainObjectStructure = domainObjectStructure;
		this.idScanner = idScanner;
	}

	public FieldValuePair[] findRecord(int domainObjectID) {
		Field[] fields = domainObjectStructure.toArray();
		if (domainObjectIsExist(domainObjectID, fields)) {
			int fieldSize = fields.length;
			FieldValuePair[] result = new FieldValuePair[fieldSize];

			try {
				int recordID = getRecordIDForDomainObjectID(domainObjectID,
						fields);
				byte[] rawData = RecordStoresManager
						.recordStoreFor(storageName).getRecord(recordID);
				DataInputStream wrapper = new DataInputStream(
						new ByteArrayInputStream(rawData));
				for (int i = 0; i < fieldSize; i++) {
					DataType type = DataType.fromInteger(wrapper.readInt());
					Field attribute = fields[i];
					Object val = readValueFrom(wrapper, type);

					result[i] = new FieldValuePair(attribute, val);
				}

				return result;
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (InvalidRecordIDException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new FieldValuePair[0];
	}

	private FieldValuePair[] getAttributeValuePairsFrom(byte[] rawData,
			Field[] fields) {
		int length = fields.length;
		FieldValuePair[] result = new FieldValuePair[length];

		try {
			DataInputStream wrapper = new DataInputStream(
					new ByteArrayInputStream(rawData));
			for (int i = 0; i < length; i++) {
				DataType type = DataType.fromInteger(wrapper.readInt());
				Field field = fields[i];
				Object val = readValueFrom(wrapper, type);

				result[i] = new FieldValuePair(field, val);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private Object readValueFrom(DataInputStream wrapper, DataType type) {
		Object value = null;
		try {
			if (type.equals(DataType.INT)) {
				value = new Integer(wrapper.readInt());
			} else if (type.equals(DataType.LONG)) {
				value = new Long(wrapper.readLong());
			} else if (type.equals(DataType.STRING)) {
				value = wrapper.readUTF();
			} else if (type.equals(DataType.DATE)) {
				value = new Date(wrapper.readLong());
			} else if (type.equals(DataType.BOOL)) {
				value = new Boolean(wrapper.readBoolean());
			} else {
				throw new IllegalArgumentException(
						"Object structure is not valid.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;
	}

	public int nextAvailableDomainObjectID() {
		try {
			return RecordStoresManager.recordStoreFor(storageName)
					.getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public void removeRecord(int domainObjectID) {
		try {
			RecordStoresManager.recordStoreFor(storageName).deleteRecord(
					domainObjectID);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void saveRecord(int domainObjectID, FieldValuePair[] data) {
		Field[] fields = getFieldsListFrom(data);
		if (domainObjectIsExist(domainObjectID, fields)) {
			editExistingRecord(domainObjectID, data);
		} else {
			insertNewRecord(data);
		}
	}

	private Field[] getFieldsListFrom(FieldValuePair[] data) {
		int size = data.length;
		Field[] result = new Field[size];

		for (int i = 0; i < size; i++) {
			result[i] = data[i].getField();
		}

		return result;
	}

	private boolean domainObjectIsExist(final int domainObjectID,
			final Field[] fields) {
		RecordFilter domainObjectIDFilter = new RecordFilter() {
			public boolean matches(byte[] arg0) {
				FieldValuePair[] data = getAttributeValuePairsFrom(arg0, fields);
				return (idScanner.scanDomainObjectIDFrom(data) == domainObjectID);
			}
		};

		try {
			RecordEnumeration re = RecordStoresManager.recordStoreFor(
					storageName).enumerateRecords(domainObjectIDFilter, null,
					false);
			if (re.hasNextElement()) {
				return true;
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		}

		return false;
	}

	private int getRecordIDForDomainObjectID(final int domainObjectID,
			final Field[] fields) {
		RecordFilter domainObjectIDFilter = new RecordFilter() {
			public boolean matches(byte[] arg0) {
				FieldValuePair[] data = getAttributeValuePairsFrom(arg0, fields);
				return (idScanner.scanDomainObjectIDFrom(data) == domainObjectID);
			}
		};

		try {
			RecordEnumeration re = RecordStoresManager.recordStoreFor(
					storageName).enumerateRecords(domainObjectIDFilter, null,
					false);
			if (re.hasNextElement()) {
				return re.nextRecordId();
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		}

		throw new IllegalArgumentException(
				"Invalid Domain Object ID specified.");
	}

	private void insertNewRecord(FieldValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getField().getFieldType();
			Object value = data[i].getValue();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			RecordStoresManager.recordStoreFor(storageName).addRecord(rawData,
					0, rawData.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	private void writeTypeAndObjectTo(DataOutputStream wrapper, DataType type,
			Object value) {
		try {
			wrapper.writeInt(type.toInteger());

			if (type == DataType.INT) {
				Integer toBeWritten = (Integer) value;
				wrapper.writeInt(toBeWritten.intValue());
			} else if (type == DataType.LONG) {
				Long toBeWritten = (Long) value;
				wrapper.writeLong(toBeWritten.longValue());
			} else if (type == DataType.STRING) {
				String toBeWritten = (String) value;
				wrapper.writeUTF(toBeWritten);
			} else if (type == DataType.DATE) {
				Date toBeWritten = (Date) value;
				wrapper.writeLong(toBeWritten.getTime());
			} else if (type == DataType.BOOL) {
				Boolean toBeWritten = (Boolean) value;
				wrapper.writeBoolean(toBeWritten.booleanValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void editExistingRecord(int domainObjectID, FieldValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getField().getFieldType();
			Object value = data[i].getValue();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			int recordID = getRecordIDForDomainObjectID(domainObjectID,
					getFieldsListFrom(data));
			RecordStoresManager.recordStoreFor(storageName).setRecord(
					recordID, rawData, 0, rawData.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	public int[] findAllDomainObjectIDs() {
		try {
			RecordEnumeration re = RecordStoresManager.recordStoreFor(
					storageName).enumerateRecords(null, null, false);
			int total = re.numRecords();
			int[] result = new int[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresManager
						.recordStoreFor(storageName).getRecord(
								re.nextRecordId());
				FieldValuePair[] data = getAttributeValuePairsFrom(rawData,
						domainObjectStructure.toArray());
				int domainObjectID = idScanner.scanDomainObjectIDFrom(data);
				result[i] = domainObjectID;
			}

			return result;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public Object buildDomainObject(int domainObjectID,
			DomainObjectFactory factory) {
		FieldValuePair[] data = findRecord(domainObjectID);
		Object domainObject = factory.createDomainObject(data);

		return domainObject;
	}

}
