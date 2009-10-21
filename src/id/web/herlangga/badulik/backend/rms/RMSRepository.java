package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;
import id.web.herlangga.badulik.definition.Tuple.*;

import java.io.*;
import java.util.*;

import javax.microedition.rms.*;

public class RMSRepository implements ObjectRepository {
	private final String objectIdRecordStoreName;
	private final String objectStateRecordStoreName;
	private final Schema objectSchema;

	RMSRepository(String objectIdRecordStoreName,
			String objectStateRecordStoreName, Schema objectSchema) {
		this.objectIdRecordStoreName = objectIdRecordStoreName;
		this.objectStateRecordStoreName = objectStateRecordStoreName;
		this.objectSchema = objectSchema;
	}

	public Object find(Element objectId, ObjectReconstitutor reconstitutor) {
		if (!contains(objectId)) {
			throw new IllegalArgumentException("Object ID is not exist.");
		}
		try {
			Tuple state = getPersistedState(objectId);
			return reconstitutor.reconstituteObject(objectId, state);
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

	public Object[] findAll(ObjectFilter filter,
			ObjectReconstitutor reconstitutor) {
		Vector matchesObjects = new Vector();

		Element[] objectIds = fetchAllIds();
		int totalStoredObjects = objectIds.length;
		for (int i = 0; i < totalStoredObjects; i++) {
			Object toCheck = find(objectIds[i], reconstitutor);
			if (filter.matches(toCheck)) {
				matchesObjects.addElement(toCheck);
			}
		}

		Object[] result = new Object[matchesObjects.size()];
		matchesObjects.copyInto(result);

		return result;
	}

	public void save(Object object, ObjectIdExtractor idExtractor,
			ObjectStateExtractor stateExtractor) {
		Element objectId = idExtractor.extractId(object);
		Tuple state = stateExtractor.extractState(object);

		if (!objectSchema.equals(state.schema())) {
			throw new IllegalArgumentException("Structure "
					+ "and extracted Object state are not equals.");
		}

		try {
			byte[] stateRawData = generateRawDataFrom(state);
			if (contains(objectId)) {
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

	public boolean contains(Element objectId) {
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

	public long nextSequenceNumber() {
		try {
			return RecordStoresGateway.recordStoreFor(objectIdRecordStoreName)
					.getNextRecordID();
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
				ids[i] = ElementReader.readFrom(rawData);
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

		throw new RuntimeException("Failed to fetch Object IDs.");
	}

	private Tuple getPersistedState(Element objectId)
			throws RecordStoreNotOpenException, InvalidRecordIDException,
			RecordStoreException, IOException {
		int recordId = translateToRecordIdFrom(objectId);
		byte[] rawData = RecordStoresGateway.recordStoreFor(
				objectStateRecordStoreName).getRecord(recordId);

		return generateStateFrom(rawData);
	}

	private Tuple generateStateFrom(byte[] rawData) throws IOException {
		int fieldSize = objectSchema.attributesSize();
		TupleBuilder tupleBuilder = Tuple.buildNew().withSchema(objectSchema);

		ByteArrayInputStream reader = new ByteArrayInputStream(rawData);
		DataInputStream wrapper = new DataInputStream(reader);
		for (int i = 0; i < fieldSize; i++) {
			Element element = ElementReader.readFrom(wrapper);
			tupleBuilder.thenAddField(objectSchema.attributeNameAt(i), element);
		}
		reader.close();
		wrapper.close();

		return tupleBuilder.thenGetResult();
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

	private byte[] generateRawDataFrom(Tuple state) throws IOException {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();

		Enumeration elementsEnum = state.elementsEnumeration();
		while (elementsEnum.hasMoreElements()) {
			writer.write(generateRawDataFrom((Element) elementsEnum
					.nextElement()));
		}

		byte[] rawData = writer.toByteArray();
		writer.close();

		return rawData;
	}

	private byte[] generateRawDataFrom(Element datum) throws IOException {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		DataOutputStream wrapper = new DataOutputStream(writer);

		ElementWriter.for_(datum.type()).writeTo(wrapper, datum);
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
				Element currentObjectId = ElementReader.readFrom(rawData);
				return currentObjectId.equals(objectId);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		}
	}

}
