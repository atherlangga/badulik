package id.web.herlangga.badulik;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * Provides basic functionality to manipulate RMS.
 * 
 * @author angga
 * 
 */
public class RMSWorker {
	private DomainObjectRepository repository;
	private RMSDomainIDMatcher idMatcher;

	/**
	 * Constructor.
	 * 
	 * @param repository
	 *            {@link DomainObjectRepository} where Domain Objects are kept.
	 * @param idMatcher
	 *            {@link RMSDomainIDMatcher} to translate Domain ID to Record
	 *            ID.
	 */
	public RMSWorker(DomainObjectRepository repository,
			RMSDomainIDMatcher idMatcher) {
		this.repository = repository;
		this.idMatcher = idMatcher;
	}

	/**
	 * Retrieve representation of Domain Object.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to look for.
	 * @return {@link InputStream} object of raw representation.
	 */
	public InputStream findRecord(long domainObjectID) {
		InputStream result = null;

		try {
			byte[] representation = getRecordStore().getRecord(
					getRecordIDFor(domainObjectID));
			result = new ByteArrayInputStream(representation);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Determine to insert record or to edit it, and then store the data into
	 * {@link RecordStore}.
	 * 
	 * @param domainObjectID
	 *            ID of the Domain Object to be persisted.
	 * @param data
	 *            {@link OutputStream} object contains data to be persisted. The
	 *            {@link OutputStream} MUST BE created by
	 *            createOutputStreamToPersistDomainObject() method.
	 * @return <code>true</code> if succeed, else <code>false</code>.
	 */
	public boolean saveRecord(long domainObjectID, OutputStream data) {
		ByteArrayOutputStream baos = (ByteArrayOutputStream) data;
		byte[] rawData = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (domainObjectIDIsExist(domainObjectID)) {
			int recordId = getRecordIDFor(domainObjectID);
			return editRecord(recordId, rawData);
		} else {
			return insertRecord(rawData);
		}
	}

	/**
	 * Remove record from {@link RecordStore}.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to look for.
	 * @return <code>true</code> if succeed, else <code>false</code>.
	 */
	public boolean removeRecord(long domainObjectID) {
		try {
			getRecordStore().deleteRecord(getRecordIDFor(domainObjectID));
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

	/**
	 * Get next available Record ID to persist Domain Object.
	 * 
	 * @return next available Record ID.
	 */
	public int nextAvailableID() {
		try {
			return getRecordStore().getNextRecordID();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Delete Record Store.
	 * 
	 * @return <code>true</code> if succeed, else <code>false</code>.
	 */
	public boolean dropRecordStorage() {
		return RMSRecordStoresManager.deleteRecordStore(repository.getName());
	}

	/**
	 * Resurrect Domain Object given its Domain Object ID and its factory.
	 * 
	 * @param domainObjectID
	 *            Domain Object's ID.
	 * @param factory
	 *            {@link DomainObjectFactory} for particular Domain Object.
	 * @return Resurrected Domain Object.
	 */
	public Object buildDomainObject(long domainObjectID,
			DomainObjectFactory factory) {
		if (domainObjectIDIsExist(domainObjectID)) {
			Object persisted = factory.createDomainObject(domainObjectID);
			return persisted;
		}
		
		return null;
	}

	/**
	 * Retrieve all Domain IDs in specified RecordStore.
	 * 
	 * @return array contains of Domain IDs.
	 */
	public long[] fetchAllIDs(DomainObjectIDFactory idFactory) {
		long[] results = new long[0];

		try {
			RecordEnumeration records = getRecordStore().enumerateRecords(null,
					null, false);
			results = new long[records.numRecords()];
			int counter = 0;
			while (records.hasNextElement()) {
				int recordId = records.nextRecordId();

				byte[] rawData = getRecordStore().getRecord(recordId);
				ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
				long id = idFactory.fetchDomainObjectIDFrom(bais);
				results[counter] = id;

				counter++;
			}
			records.destroy();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Create writer to persist Domain Object.
	 * 
	 * @return {@link OutputStream} to write to.
	 */
	public OutputStream createOutputStreamToPersistDomainObject() {
		return new ByteArrayOutputStream();
	}

	/**
	 * Read value for specified field number.
	 * 
	 * @param row
	 *            {@link InputStream} to be read.
	 * @return {@link Object} if exist, else <code>null</code>.
	 */
	public Object fetchValueFrom(InputStream row, String name) {
		DataInputStream dis;
		if (row instanceof DataInputStream) {
			dis = (DataInputStream) row;
		} else {
			dis = new DataInputStream(row);
		}

		try {
			dis.reset();
			int remaining = skipRecordUpTo(name, dis);
			if (remaining > 0) {
				return readValueFor(name, dis);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private RecordStore getRecordStore() {
		return RMSRecordStoresManager.recordStoreFor(repository.getName());
	}

	private int getRecordIDFor(long domainObjectID) {
		int result = 0;
		try {
			RecordEnumeration enumerator = getRecordStore().enumerateRecords(
					idMatcher.getRecordFilterMatcherFor(domainObjectID), null,
					false);
			if (enumerator.hasNextElement()) {
				result = enumerator.nextRecordId();
			}
			enumerator.destroy();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		}

		return result;
	}

	private boolean domainObjectIDIsExist(long domainObjectIDToBeChecked) {
		boolean found = false;
		try {
			RecordEnumeration enumerator = getRecordStore()
					.enumerateRecords(
							idMatcher
									.getRecordFilterMatcherFor(domainObjectIDToBeChecked),
							null, false);
			if (enumerator.hasNextElement()) {
				found = true;
			}
			enumerator.destroy();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		}

		return found;
	}

	private boolean editRecord(int recordID, byte[] data) {
		try {
			getRecordStore().setRecord(recordID, data, 0, data.length);
			return true;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean insertRecord(byte[] data) {
		try {
			getRecordStore().addRecord(data, 0, data.length);
			return true;
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

		return false;
	}

	private int skipRecordUpTo(String name, DataInputStream reader)
			throws IOException {
		int remaining = reader.available();
		Structure structure = repository.getStructure();

		int counter = 0;
		while ((structure.getNameNumber(counter) != name) && (remaining >= 0)) {
			Type toBeRead = structure.getTypeNumber(counter);
			if (toBeRead == Type.BOOL) {
				reader.skipBytes(2);
				remaining -= 2;
			} else if (toBeRead == Type.INT) {
				reader.skipBytes(4);
				remaining -= 4;
			} else if (toBeRead == Type.LONG) {
				reader.skipBytes(8);
				remaining -= 8;
			} else if (toBeRead == Type.DATE) {
				reader.skipBytes(8);
				remaining -= 8;
			} else if (toBeRead == Type.STRING) {
				remaining -= reader.readUTF().getBytes().length;
			}

			counter++;
		}

		return remaining;
	}

	private Object readValueFor(String name, DataInputStream reader)
			throws IOException {
		Object result = null;
		Structure structure = repository.getStructure();
		Type toBeRead = structure.getTypeOf(name);

		if (toBeRead == Type.INT) {
			result = new Integer(reader.readInt());
		} else if (toBeRead == Type.LONG) {
			result = new Long(reader.readLong());
		} else if (toBeRead == Type.DATE) {
			result = new Date(reader.readLong());
		} else if (toBeRead == Type.STRING) {
			result = reader.readUTF();
		} else if (toBeRead == Type.BOOL) {
			result = new Boolean(reader.readBoolean());
		}

		reader.close();
		return result;
	}

}
