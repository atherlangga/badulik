package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.Date;

import javax.microedition.rms.*;

public class ObjectRepositoryRMS implements ObjectRepository {
	private final String recordStoreName;
	private final Structure objectStructure;

	ObjectRepositoryRMS(String recordStoreName, Structure objectStructure) {
		this.recordStoreName = recordStoreName;
		this.objectStructure = objectStructure;
	}

	public Datum[] find(long id) {
		if (isExist(id)) {
			try {
				int recordID = getRecordIDForDomainObjectID(id);
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						recordStoreName).getRecord(recordID);
				return getDataTypeAndValuePairsFrom(rawData);
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (InvalidRecordIDException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}

		return new Datum[0];
	}

	private Datum[] getDataTypeAndValuePairsFrom(byte[] rawData) {
		int fieldSize = objectStructure.fieldsSize();
		Datum[] result = new Datum[fieldSize];

		try {
			ByteArrayInputStream reader = new ByteArrayInputStream(rawData);
			DataInputStream wrapper = new DataInputStream(reader);
			for (int i = 0; i < fieldSize; i++) {
				Type type = Type.fromInteger(wrapper.readInt());
				Object val = readValueFrom(wrapper, type);

				result[i] = new Datum(type, val);
			}

			reader.close();
			wrapper.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private Object readValueFrom(DataInputStream wrapper, Type type) {
		try {
			Object value;
			if (type.equals(Type.INT)) {
				value = new Integer(wrapper.readInt());
			} else if (type.equals(Type.LONG)) {
				value = new Long(wrapper.readLong());
			} else if (type.equals(Type.STRING)) {
				value = wrapper.readUTF();
			} else if (type.equals(Type.DATE)) {
				value = new Date(wrapper.readLong());
			} else if (type.equals(Type.BOOL)) {
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

	public long nextAvailableID() {
		try {
			return RecordStoresGateway.recordStoreFor(recordStoreName)
					.getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("This line shouldn't be reached.");
	}

	public void remove(long id) {
		try {
			int recordID = getRecordIDForDomainObjectID(id);
			RecordStoresGateway.recordStoreFor(recordStoreName).deleteRecord(
					recordID);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void save(long id, Datum[] data) {
		if (isExist(id)) {
			editExistingRecord(id, data);
		} else {
			insertNewRecord(data);
		}
	}

	public boolean isExist(long id) {
		RecordFilter domainObjectIDFilter = getDomainObjectIDRecordFilterFor(id);
		boolean result = false;
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(domainObjectIDFilter,
					null, false);
			if (re.hasNextElement()) {
				result = true;
			}

			re.destroy();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		}

		return result;
	}

	private RecordFilter getDomainObjectIDRecordFilterFor(
			final long domainObjectID) {
		final int domainObjectIDFieldNumber = objectStructure
				.objectIdFieldNumber();

		RecordFilter domainObjectIDFilter = new RecordFilter() {
			public boolean matches(byte[] rawData) {
				Datum[] data = getDataTypeAndValuePairsFrom(rawData);

				long id = ((Long) data[domainObjectIDFieldNumber].value())
						.longValue();
				return (id == domainObjectID);
			}
		};

		return domainObjectIDFilter;
	}

	private int getRecordIDForDomainObjectID(long domainObjectID) {
		RecordFilter domainObjectIDFilter = getDomainObjectIDRecordFilterFor(domainObjectID);
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(domainObjectIDFilter,
					null, false);
			if (re.hasNextElement()) {
				int result = re.nextRecordId();
				re.destroy();
				return result;
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		}

		throw new IllegalArgumentException(
				"Invalid Domain Object ID specified.");
	}

	private void insertNewRecord(Datum[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			Type type = data[i].type();
			Object value = data[i].value();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			RecordStoresGateway.recordStoreFor(recordStoreName).addRecord(
					rawData, 0, rawData.length);
			writer.close();
			wrapper.close();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeTypeAndObjectTo(DataOutputStream wrapper, Type type,
			Object value) {
		try {
			wrapper.writeInt(type.typeAsInteger());

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

	private void editExistingRecord(long domainObjectID,
			Datum[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			Type type = data[i].type();
			Object value = data[i].value();

			writeTypeAndObjectTo(wrapper, type, value);
		}

		byte[] rawData = writer.toByteArray();
		try {
			int recordID = getRecordIDForDomainObjectID(domainObjectID);
			RecordStoresGateway.recordStoreFor(recordStoreName).setRecord(
					recordID, rawData, 0, rawData.length);
			writer.close();
			wrapper.close();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public long[] fetchAllIDs() {
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(null, null, false);
			int total = re.numRecords();
			long[] result = new long[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						recordStoreName).getRecord(re.nextRecordId());
				Datum[] data = getDataTypeAndValuePairsFrom(rawData);
				Long id = (Long) data[objectStructure
						.objectIdFieldNumber()].value();
				long domainObjectID = id.longValue();
				result[i] = domainObjectID;
			}
			re.destroy();

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

	public Object build(long id, ObjectFactory factory) {
		Datum[] data = find(id);
		Object domainObject = factory.createDomainObject(data);

		return domainObject;
	}

}
