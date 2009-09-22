package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Datum;

/**
 * Object repository.
 * 
 * @author angga
 * 
 */
public interface ObjectRepository {
	/**
	 * Fetch data based on specified object ID.
	 * 
	 * @param objectID
	 *            Object ID whose data is going to be fetched.
	 * @return array of {@link Datum} contains data for specified
	 *         object ID.
	 */
	public Datum[] find(long objectID);

	/**
	 * Store object with specified data. Based on the supplied object ID,
	 * {@link ObjectRepository} will make decision whether to insert new record
	 * or edit existing record.
	 * 
	 * @param objectID
	 *            Object ID.
	 * @param data
	 *            Array of {@link Datum} contains data for the
	 *            specified object.
	 */
	public void save(long objectID, Datum[] data);

	/**
	 * Delete specified object based on its ID.
	 * 
	 * @param objectID
	 *            Object ID to delete from storage.
	 */
	public void remove(long objectID);

	/**
	 * Check for object existance based on its ID.
	 * 
	 * @param objectID
	 *            Object ID to search.
	 * @return <code>true</code> if specified object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(long objectID);

	/**
	 * Find and return all object IDs exist in the storage.
	 * 
	 * @return array of long contains object IDs.
	 */
	public long[] fetchAllIDs();

	/**
	 * Get new and valid object ID. When the object ID is retrieved, it is
	 * recommended to immediately save the object to avoid collision with the
	 * next caller of this function.
	 * 
	 * @return available object ID on the storage.
	 */
	public long nextAvailableID();

	/**
	 * Build object with specified object ID.
	 * 
	 * @param objectID
	 *            Domain Object ID to be built.
	 * @param factory
	 *            {@link ObjectFactory} to be delegated the job of the object
	 *            building.
	 * @return Object with specified ID.
	 */
	public Object build(long objectID, ObjectFactory factory);
}
