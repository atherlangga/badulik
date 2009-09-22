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

	public Datum[] find(long objectId) {
		if (isExist(objectId)) {
			try {
				int recordId = translateToRecordIdFrom(objectId);
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						recordStoreName).getRecord(recordId);
				return generateDataFrom(rawData);
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

	private Datum[] generateDataFrom(byte[] rawData) {
		int fieldSize = objectStructure.fieldsSize();
		Datum[] result = new Datum[fieldSize];

		try {
			ByteArrayInputStream reader = new ByteArrayInputStream(rawData);
			DataInputStream wrapper = new DataInputStream(reader);
			for (int i = 0; i < fieldSize; i++) {
				Type type = Type.fromInteger(wrapper.readInt());
				Object value = readValueFrom(wrapper, type);

				result[i] = new Datum(type, value);
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

	public long nextAvailableId() {
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

	public void remove(long objectId) {
		try {
			int recordId = translateToRecordIdFrom(objectId);
			RecordStoresGateway.recordStoreFor(recordStoreName).deleteRecord(
					recordId);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void save(long objectId, Datum[] data) {
		if (isExist(objectId)) {
			editExistingRecord(objectId, data);
		} else {
			insertNewRecord(data);
		}
	}

	public boolean isExist(long objectId) {
		RecordFilter objectIdFilter = createRecordFilterFor(objectId);
		boolean result = false;
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(objectIdFilter, null,
					false);
			if (re.hasNextElement()) {
				result = true;
			}

			re.destroy();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		}

		return result;
	}

	private RecordFilter createRecordFilterFor(final long objectId) {
		final int objectIFieldNumber = objectStructure.objectIdFieldNumber();

		RecordFilter domainObjectIdFilter = new RecordFilter() {
			public boolean matches(byte[] rawData) {
				Datum[] data = generateDataFrom(rawData);

				long id = ((Long) data[objectIFieldNumber].value())
						.longValue();
				return (id == objectId);
			}
		};

		return domainObjectIdFilter;
	}

	private int translateToRecordIdFrom(long objectId) {
		RecordFilter domainObjectIDFilter = createRecordFilterFor(objectId);
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

		throw new IllegalArgumentException("Invalid object ID specified.");
	}

	private void insertNewRecord(Datum[] data) {
		byte[] rawData = generateRawDataFrom(data);
		try {
			RecordStoresGateway.recordStoreFor(recordStoreName).addRecord(
					rawData, 0, rawData.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] generateRawDataFrom(Datum[] data) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = data.length;
		for (int i = 0; i < dataFieldLength; i++) {
			writeDatumTo(wrapper, data[i]);
		}
		
		byte[] result = writer.toByteArray();
		try {
			writer.close();
			wrapper.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private void writeDatumTo(DataOutputStream wrapper, Datum datum) {
		try {
			Type type = datum.type();
			wrapper.writeInt(type.typeAsInteger());

			if (type == Type.INT) {
				Integer toBeWritten = (Integer) datum.value();
				wrapper.writeInt(toBeWritten.intValue());
			} else if (type == Type.LONG) {
				Long toBeWritten = (Long) datum.value();
				wrapper.writeLong(toBeWritten.longValue());
			} else if (type == Type.STRING) {
				String toBeWritten = (String) datum.value();
				wrapper.writeUTF(toBeWritten);
			} else if (type == Type.DATE) {
				Date toBeWritten = (Date) datum.value();
				wrapper.writeLong(toBeWritten.getTime());
			} else if (type == Type.BOOL) {
				Boolean toBeWritten = (Boolean) datum.value();
				wrapper.writeBoolean(toBeWritten.booleanValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void editExistingRecord(long objectID, Datum[] data) {
		byte[] rawData = generateRawDataFrom(data);
		try {
			int recordId = translateToRecordIdFrom(objectID);
			RecordStoresGateway.recordStoreFor(recordStoreName).setRecord(
					recordId, rawData, 0, rawData.length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	public long[] fetchAllIds() {
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(null, null, false);
			int total = re.numRecords();
			long[] result = new long[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						recordStoreName).getRecord(re.nextRecordId());
				Datum[] data = generateDataFrom(rawData);
				Long id = (Long) data[objectStructure.objectIdFieldNumber()]
						.value();
				long objectId = id.longValue();
				result[i] = objectId;
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

	public Object build(long objectId, ObjectFactory factory) {
		Datum[] data = find(objectId);
		Object domainObject = factory.createDomainObject(data);

		return domainObject;
	}

}
