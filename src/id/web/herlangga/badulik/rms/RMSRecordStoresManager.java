package id.web.herlangga.badulik.rms;

import java.util.Hashtable;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * Provides functionality to manage {@link RecordStore}s.
 * 
 * @author angga
 * 
 */
public final class RMSRecordStoresManager {
	private static Hashtable recordStores = new Hashtable();

	/**
	 * Retrieve {@link RecordStore} with some name.
	 * 
	 * @param repositoryName
	 *            name of the {@link RecordStore} to retrieve.
	 * @return existing {@link RecordStore} with specified name, or new
	 *         {@link RecordStore} if not exist yet.
	 */
	public static RecordStore recordStoreFor(String repositoryName) {
		if (!recordStores.containsKey(repositoryName)) {
			createRecordStore(repositoryName);
		}

		return (RecordStore) recordStores.get(repositoryName);
	}

	/**
	 * Delete {@link RecordStore} with specified name.
	 * 
	 * @param name
	 *            name of the RecordStore to be deleted.
	 * @return <code>true</code> if succeed, else <code>false</code>.
	 */
	public static boolean deleteRecordStore(String name) {
		closeThenDelete(name);
		if (recordStoreIsNotExist(name)) {
			recordStores.remove(name);
			return true;
		}

		return false;
	}

	/**
	 * Check whether specified {@link RecordStore} is exist.
	 * 
	 * @param name
	 *            name to be checked.
	 * @return <code>true</code> if {@link RecordStore} with specified name is
	 *         not exist, else <code>false</code>.
	 */
	public static boolean isExist(String name) {
		return !recordStoreIsNotExist(name);
	}

	private static void closeThenDelete(String name) {
		try {
			RecordStore repository = recordStoreFor(name); 
			repository.closeRecordStore();
			RecordStore.deleteRecordStore(name);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	private static void createRecordStore(String repositoryName) {
		try {
			boolean createIfNecessary = true;
			RecordStore recordStore = RecordStore.openRecordStore(
					repositoryName, createIfNecessary);
			recordStores.put(repositoryName, recordStore);
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	private static boolean recordStoreIsNotExist(String name) {
		String[] existingRecordStores = RecordStore.listRecordStores();
		if (existingRecordStores != null) {
			int count = existingRecordStores.length;
			for (int i = 0; i < count; i++) {
				if (name == existingRecordStores[i]) {
					return false;
				}
			}
		}
		return true;
	}
}
