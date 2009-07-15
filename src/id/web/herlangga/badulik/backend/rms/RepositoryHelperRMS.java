package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.DomainObjectFactory;
import id.web.herlangga.badulik.RepositoryHelper;
import id.web.herlangga.badulik.definition.DataType;
import id.web.herlangga.badulik.definition.DataTypeAndValuePair;
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

	public RepositoryHelperRMS(String storageName,
			Structure domainObjectStructure) {
		this.storageName = storageName;
		this.domainObjectStructure = domainObjectStructure;
	}

	public DataTypeAndValuePair[] findRecord(long domainObjectID) {
		if (domainObjectIsExist(domainObjectID)) {
			try {
				int recordID = getRecordIDForDomainObjectID(domainObjectID);
				byte[] rawData = RecordStoresManager
						.recordStoreFor(storageName).getRecord(recordID);
				return getDataTypeAndValuePairsFrom(rawData);
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (InvalidRecordIDException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}

		return new DataTypeAndValuePair[0];
	}

	private DataTypeAndValuePair[] getDataTypeAndValuePairsFrom(byte[] rawData) {
		int fieldSize = domainObjectStructure.fieldsSize();
		DataTypeAndValuePair[] result = new DataTypeAndValuePair[fieldSize];

		try {
			DataInputStream wrapper = new DataInputStream(
					new ByteArrayInputStream(rawData));
			for (int i = 0; i < fieldSize; i++) {
				DataType type = DataType.fromInteger(wrapper.readInt());
				Object val = readValueFrom(wrapper, type);

				result[i] = new DataTypeAndValuePair(type, val);
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

			return value;
		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("This line shouldn't be reached.");
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

	public void removeRecord(long domainObjectID) {
		try {
			int recordID = getRecordIDForDomainObjectID(domainObjectID);
			RecordStoresManager.recordStoreFor(storageName).deleteRecord(
					recordID);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void saveRecord(long domainObjectID, DataTypeAndValuePair[] data) {
		if (domainObjectIsExist(domainObjectID)) {
			editExistingRecord(domainObjectID, data);
		} else {
			insertNewRecord(data);
		}
	}

	private boolean domainObjectIsExist(long domainObjectID) {
		RecordFilter domainObjectIDFilter = getDomainObjectIDRecordFilterFor(domainObjectID);

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

	private RecordFilter getDomainObjectIDRecordFilterFor(
			final long domainObjectID) {
		final int domainObjectIDFieldNumber = domainObjectStructure
				.getDomainObjectIDFieldNumber();
		RecordFilter domainObjectIDFilter = new RecordFilter() {
			public boolean matches(byte[] arg0) {
				DataTypeAndValuePair[] data = getDataTypeAndValuePairsFrom(arg0);
				Long id = (Long) data[domainObjectIDFieldNumber].getValue();
				long currentDomainObjectID = id.longValue();
				return (currentDomainObjectID == domainObjectID);
			}
		};
		return domainObjectIDFilter;
	}

	private int getRecordIDForDomainObjectID(long domainObjectID) {
		RecordFilter domainObjectIDFilter = getDomainObjectIDRecordFilterFor(domainObjectID);

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

	private void insertNewRecord(DataTypeAndValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getDataType();
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

	private void editExistingRecord(long domainObjectID,
			DataTypeAndValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getDataType();
			Object value = data[i].getValue();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			int recordID = getRecordIDForDomainObjectID(domainObjectID);
			RecordStoresManager.recordStoreFor(storageName).setRecord(recordID,
					rawData, 0, rawData.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	public long[] findAllDomainObjectIDs() {
		try {
			RecordEnumeration re = RecordStoresManager.recordStoreFor(
					storageName).enumerateRecords(null, null, false);
			int total = re.numRecords();
			long[] result = new long[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresManager
						.recordStoreFor(storageName).getRecord(
								re.nextRecordId());
				DataTypeAndValuePair[] data = getDataTypeAndValuePairsFrom(rawData);
				Long id = (Long) data[domainObjectStructure
						.getDomainObjectIDFieldNumber()].getValue();
				long domainObjectID = id.longValue();
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

	public Object buildDomainObject(long domainObjectID,
			DomainObjectFactory factory) {
		DataTypeAndValuePair[] data = findRecord(domainObjectID);
		Object domainObject = factory.createDomainObject(data);

		return domainObject;
	}

}
