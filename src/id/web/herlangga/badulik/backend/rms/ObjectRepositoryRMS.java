package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

import java.io.*;

import javax.microedition.rms.*;

public class ObjectRepositoryRMS implements ObjectRepository {
	private final String recordStoreName;
	private final Structure objectStructure;

	ObjectRepositoryRMS(String recordStoreName, Structure objectStructure) {
		this.recordStoreName = recordStoreName;
		this.objectStructure = objectStructure;
	}

	public Object find(Datum objectId, ObjectReconstitutor reconstitutor) {
		if (!isExist(objectId)) {
			throw new IllegalArgumentException("Object ID is not exist");
		}
		try {
			Datum[] states = getPersistedStates(objectId);
			return reconstitutor.reconstituteObjectFrom(states);
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

	public void save(Object object, ObjectStateExtractor extractor) {
		Datum[] states = extractor.extractStatesFrom(object);
		if (!objectStructure.compatibleWith(states)) {
			throw new IllegalArgumentException("Incompatible Structure "
					+ "and extracted Object state");
		}
		Datum objectId = states[objectStructure.idFieldNumber()];

		try {
			byte[] rawData = generateRawDataFrom(states);
			if (isExist(objectId)) {
				editExisting(objectId, rawData);
			} else {
				insertNew(rawData);
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public void remove(Datum objectId) {
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

	public boolean isExist(Datum objectId) {
		RecordFilter objectIdFilter = new ObjectIdFilter(objectId);
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

	public long generateSequenceValue() {
		try {
			return RecordStoresGateway.recordStoreFor(recordStoreName)
					.getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public Datum[] fetchAllIds() {
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					recordStoreName).enumerateRecords(null, null, false);
			int total = re.numRecords();
			Datum[] ids = new Datum[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						recordStoreName).getRecord(re.nextRecordId());
				Datum[] states = generateStatesFrom(rawData);
				ids[i] = states[objectStructure.idFieldNumber()];
			}
			re.destroy();

			return ids;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("This line shouldn't be reached.");
	}

	private Datum[] getPersistedStates(Datum objectId)
			throws RecordStoreNotOpenException, InvalidRecordIDException,
			RecordStoreException, IOException {
		int recordId = translateToRecordIdFrom(objectId);
		byte[] rawData = RecordStoresGateway.recordStoreFor(recordStoreName)
				.getRecord(recordId);

		return generateStatesFrom(rawData);
	}

	private Datum[] generateStatesFrom(byte[] rawData) throws IOException {
		int fieldSize = objectStructure.fieldsSize();
		Datum[] states = new Datum[fieldSize];

		ByteArrayInputStream reader = new ByteArrayInputStream(rawData);
		DataInputStream wrapper = new DataInputStream(reader);
		for (int i = 0; i < fieldSize; i++) {
			states[i] = DatumReader.of(wrapper).readFrom(wrapper);
		}
		reader.close();
		wrapper.close();

		return states;
	}

	private int translateToRecordIdFrom(Datum objectId)
			throws RecordStoreNotOpenException, InvalidRecordIDException {
		RecordFilter objectIdFilter = new ObjectIdFilter(objectId);
		RecordEnumeration re = RecordStoresGateway.recordStoreFor(
				recordStoreName).enumerateRecords(objectIdFilter, null, false);

		if (re.hasNextElement()) {
			int result = re.nextRecordId();
			re.destroy();

			return result;
		}

		throw new IllegalArgumentException("Invalid object Id specified.");
	}

	private void insertNew(byte[] rawData) throws IOException,
			RecordStoreNotOpenException, RecordStoreFullException,
			RecordStoreException {
		RecordStoresGateway.recordStoreFor(recordStoreName).addRecord(rawData,
				0, rawData.length);
	}

	private void editExisting(Datum objectId, byte[] rawData)
			throws IOException, RecordStoreNotOpenException,
			InvalidRecordIDException, RecordStoreFullException,
			RecordStoreException {
		int recordId = translateToRecordIdFrom(objectId);
		RecordStoresGateway.recordStoreFor(recordStoreName).setRecord(recordId,
				rawData, 0, rawData.length);
	}

	private byte[] generateRawDataFrom(Datum[] states) throws IOException {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		int dataFieldLength = states.length;
		for (int i = 0; i < dataFieldLength; i++) {
			DatumWriter.forDatum(states[i]).writeTo(wrapper, states[i]);
		}

		byte[] rawData = writer.toByteArray();
		writer.close();
		wrapper.close();

		return rawData;
	}

	private class ObjectIdFilter implements RecordFilter {
		private final Datum objectId;

		public ObjectIdFilter(Datum objectId) {
			this.objectId = objectId;
		}

		public boolean matches(byte[] rawData) {
			try {
				Datum[] states = generateStatesFrom(rawData);
				return objectId.equals(states[objectStructure.idFieldNumber()]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}
	}

}
