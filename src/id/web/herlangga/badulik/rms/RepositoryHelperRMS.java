package id.web.herlangga.badulik.rms;

import id.web.herlangga.badulik.Attribute;
import id.web.herlangga.badulik.AttributeValuePair;
import id.web.herlangga.badulik.RepositoryHelper;
import id.web.herlangga.badulik.Structure;
import id.web.herlangga.badulik.Type;

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
				Type type = Type.fromInteger(wrapper.readInt());
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

	private Object readValueFrom(DataInputStream wrapper, Type type)
			throws IOException {
		Object value = null;
		if (type == Type.INT) {
			value = new Integer(wrapper.readInt());
		} else if (type == Type.LONG) {
			value = new Long(wrapper.readLong());
		} else if (type == Type.STRING) {
			value = wrapper.readUTF();
		} else if (type == Type.DATE) {
			value = new Date(wrapper.readLong());
		} else if (type == Type.BOOL) {
			value = new Boolean(wrapper.readBoolean());
		} else {
			throw new IllegalArgumentException(
					"Object structure is not valid");
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
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			return false;
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void insertNewRecord(AttributeValuePair[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			Type type = data[i].getAttribute().getType();
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

	private void writeTypeAndObjectTo(DataOutputStream wrapper, Type type,
			Object value) {
		try {
			wrapper.writeInt(type.toInteger());

			if (type == Type.INT) {
				Integer toBeWritten = (Integer) value;
				wrapper.writeInt(toBeWritten.intValue());
			} else if (type == Type.LONG) {
				Long toBeWritten = (Long) value;
				wrapper.writeLong(toBeWritten.longValue());
			} else if (type == Type.STRING) {
				String toBeWritten = (String) value;
				wrapper.writeUTF(toBeWritten);
			} else if (type == Type.DATE) {
				Date toBeWritten = (Date) value;
				wrapper.writeLong(toBeWritten.getTime());
			} else if (type == Type.BOOL) {
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
			Type type = data[i].getAttribute().getType();
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

}
