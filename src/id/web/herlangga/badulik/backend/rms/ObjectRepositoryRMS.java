package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

import java.io.*;

import javax.microedition.rms.*;

public class ObjectRepositoryRMS implements ObjectRepository {
	private final String objectIdRecordStoreName;
	private final String objectStateRecordStoreName;
	private final Structure objectStateStructure;

	ObjectRepositoryRMS(String objectIdRecordStoreName,
			String objectStateRecordStoreName, Structure objectStateStructure) {
		this.objectIdRecordStoreName = objectIdRecordStoreName;
		this.objectStateRecordStoreName = objectStateRecordStoreName;
		this.objectStateStructure = objectStateStructure;
	}

	public Object find(Element objectId, ObjectReconstitutor reconstitutor) {
		if (!isExist(objectId)) {
			throw new IllegalArgumentException("Object ID is not exist");
		}
		try {
			Element[] state = getPersistedState(objectId);
			return reconstitutor.reconstituteObjectWith(objectId, state);
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

	public void save(Object object, ObjectIdExtractor idExtractor,
			ObjectStateExtractor stateExtractor) {
		Element objectId = idExtractor.extractIdFrom(object);
		Element[] state = stateExtractor.extractStateFrom(object);

		if (!objectStateStructure.compatibleWith(state)) {
			throw new IllegalArgumentException("Incompatible Structure "
					+ "and extracted Object state");
		}

		try {
			byte[] stateRawData = generateRawDataFrom(state);
			if (isExist(objectId)) {
				editExisting(objectId, stateRawData);
			} else {
				byte[] objectIdRawData = generateRawDataFrom(objectId);
				insertNew(objectIdRawData, stateRawData);
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

	public void remove(Element objectId) {
		try {
			int recordId = translateToRecordIdFrom(objectId);
			RecordStoresGateway.recordStoreFor(objectIdRecordStoreName)
					.deleteRecord(recordId);
			RecordStoresGateway.recordStoreFor(objectStateRecordStoreName)
					.deleteRecord(recordId);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public boolean isExist(Element objectId) {
		RecordFilter objectIdFilter = new ObjectIdFilter(objectId);
		boolean result = false;

		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					objectIdRecordStoreName).enumerateRecords(objectIdFilter,
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

	public long generateSequenceValue() {
		try {
			return RecordStoresGateway.recordStoreFor(
					objectStateRecordStoreName).getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		throw new RuntimeException();
	}

	public Element[] fetchAllIds() {
		try {
			RecordEnumeration re = RecordStoresGateway.recordStoreFor(
					objectIdRecordStoreName)
					.enumerateRecords(null, null, false);
			int total = RecordStoresGateway.recordStoreFor(
					objectIdRecordStoreName).getNumRecords();
			Element[] ids = new Element[total];

			for (int i = 0; i < total; i++) {
				byte[] rawData = RecordStoresGateway.recordStoreFor(
						objectIdRecordStoreName).getRecord(re.nextRecordId());
				ids[i] = DatumReader.readFrom(rawData);
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

	private Element[] getPersistedState(Element objectId)
			throws RecordStoreNotOpenException, InvalidRecordIDException,
			RecordStoreException, IOException {
		int recordId = translateToRecordIdFrom(objectId);
		byte[] rawData = RecordStoresGateway.recordStoreFor(
				objectStateRecordStoreName).getRecord(recordId);

		return generateStateFrom(rawData);
	}

	private Element[] generateStateFrom(byte[] rawData) throws IOException {
		int fieldSize = objectStateStructure.fieldsSize();
		Element[] state = new Element[fieldSize];

		ByteArrayInputStream reader = new ByteArrayInputStream(rawData);
		DataInputStream wrapper = new DataInputStream(reader);
		for (int i = 0; i < fieldSize; i++) {
			state[i] = DatumReader.readFrom(wrapper);
		}
		reader.close();
		wrapper.close();

		return state;
	}

	private int translateToRecordIdFrom(Element objectId)
			throws RecordStoreNotOpenException, InvalidRecordIDException {
		RecordFilter objectIdFilter = new ObjectIdFilter(objectId);
		RecordEnumeration re = RecordStoresGateway.recordStoreFor(
				objectIdRecordStoreName).enumerateRecords(objectIdFilter, null,
				false);

		if (re.hasNextElement()) {
			int result = re.nextRecordId();
			re.destroy();

			return result;
		}

		throw new IllegalArgumentException("Invalid Object ID specified.");
	}

	private void insertNew(byte[] objectIdRawData, byte[] objectStateRawData)
			throws IOException, RecordStoreNotOpenException,
			RecordStoreFullException, RecordStoreException {
		RecordStoresGateway.recordStoreFor(objectIdRecordStoreName).addRecord(
				objectIdRawData, 0, objectIdRawData.length);
		RecordStoresGateway.recordStoreFor(objectStateRecordStoreName)
				.addRecord(objectStateRawData, 0, objectStateRawData.length);
	}

	private void editExisting(Element objectId, byte[] objectStateRawData)
			throws IOException, RecordStoreNotOpenException,
			InvalidRecordIDException, RecordStoreFullException,
			RecordStoreException {
		int recordId = translateToRecordIdFrom(objectId);
		RecordStoresGateway.recordStoreFor(objectStateRecordStoreName)
				.setRecord(recordId, objectStateRawData, 0,
						objectStateRawData.length);
	}

	private byte[] generateRawDataFrom(Element[] state) throws IOException {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();

		int dataFieldLength = state.length;
		for (int i = 0; i < dataFieldLength; i++) {
			writer.write(generateRawDataFrom(state[i]));
		}

		byte[] rawData = writer.toByteArray();
		writer.close();

		return rawData;
	}

	private byte[] generateRawDataFrom(Element datum) throws IOException {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		DatumWriter.for_(datum.type()).writeTo(wrapper, datum);
		byte[] rawData = writer.toByteArray();

		writer.close();
		wrapper.close();

		return rawData;
	}

	private class ObjectIdFilter implements RecordFilter {
		private final Element objectId;

		private ObjectIdFilter(Element objectId) {
			this.objectId = objectId;
		}

		public boolean matches(byte[] rawData) {
			try {
				Element currentObjectId = DatumReader.readFrom(rawData);
				return currentObjectId.equals(objectId);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}
	}

}
