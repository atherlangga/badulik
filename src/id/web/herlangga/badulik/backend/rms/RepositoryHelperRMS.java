package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.DomainObjectFactory;
import id.web.herlangga.badulik.RepositoryHelper;
import id.web.herlangga.badulik.definition.Attribute;
import id.web.herlangga.badulik.definition.AttributeValuePair;
import id.web.herlangga.badulik.definition.DataType;
import id.web.herlangga.badulik.definition.Structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class RepositoryHelperRMS implements RepositoryHelper {
	private RecordStore storage;

	public RepositoryHelperRMS(RecordStore storage) {
		this.storage = storage;
	}

	public AttributeValuePair[] findRecord(int domainObjectID,
			Structure objectStructure) {
		int fieldSize = objectStructure.fieldSize();
		AttributeValuePair[] result = new AttributeValuePair[fieldSize];
		try {
			byte[] rawData = storage.getRecord(domainObjectID);
			DataInputStream wrapper = new DataInputStream(
					new ByteArrayInputStream(rawData));
			for (int i = 0; i < fieldSize; i++) {
				DataType type = DataType.fromInteger(wrapper.readInt());
				Attribute attribute = objectStructure.getAttributeNumber(i);
				Object val = readValueFrom(wrapper, type);

				result[i] = new AttributeValuePair(attribute, val);
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

		throw new RuntimeException();
	}

	private Object readValueFrom(DataInputStream wrapper, DataType type)
			throws IOException {
		Object value = null;
		if (type == DataType.INT) {
			value = new Integer(wrapper.readInt());
		} else if (type == DataType.LONG) {
			value = new Long(wrapper.readLong());
		} else if (type == DataType.STRING) {
			value = wrapper.readUTF();
		} else if (type == DataType.DATE) {
			value = new Date(wrapper.readLong());
		} else if (type == DataType.BOOL) {
			value = new Boolean(wrapper.readBoolean());
		} else {
			throw new IllegalArgumentException("Object structure is not valid");
		}

		return value;
	}

	public int nextAvailableDomainObjectID() {
		try {
			return storage.getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public void removeRecord(int domainObjectID) {
		try {
			storage.deleteRecord(domainObjectID);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void saveRecord(int domainObjectID, AttributeValuePair[] data) {
		if (domainObjectIsExist(domainObjectID)) {
			editExistingRecord(domainObjectID, data);
		} else {
			insertNewRecord(data);
		}
	}

	private boolean domainObjectIsExist(int domainObjectID) {
		try {
			storage.getRecord(domainObjectID);
			
			return true;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return false;
	}

	private void insertNewRecord(AttributeValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getAttribute().getType();
			Object value = data[i].getValue();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			storage.addRecord(rawData, 0, data.length);
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

	private void editExistingRecord(int domainObjectID,
			AttributeValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DataType type = data[i].getAttribute().getType();
			Object value = data[i].getValue();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			storage.setRecord(domainObjectID, rawData, 0, data.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	public int[] findAllDomainObjectIDs() {
		RecordEnumeration re;
		try {
			re = storage.enumerateRecords(null, null, false);
			int total = re.numRecords();
			int[] result = new int[total];

			for (int i = 0; i < total; i++) {
				result[i] = re.nextRecordId();
			}

			return result;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public Object buildDomainObject(int domainObjectID,
			Structure objectStructure, DomainObjectFactory factory) {
		AttributeValuePair[] data = findRecord(domainObjectID, objectStructure);
		Object domainObject = factory.createDomainObject(data);
		
		return domainObject;
	}

}
