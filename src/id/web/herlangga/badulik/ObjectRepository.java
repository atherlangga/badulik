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
	 * Fetch data based on specified object Id.
	 * 
	 * @param objectId
	 *            Object Id whose data is going to be fetched.
	 * @return array of {@link Datum} contains data for specified object Id.
	 */
	public Datum[] find(long objectId);

	/**
	 * Store object with specified data. Based on the supplied object Id,
	 * {@link ObjectRepository} will make decision whether to insert new record
	 * or edit existing record.
	 * 
	 * @param objectId
	 *            Object Id.
	 * @param data
	 *            Array of {@link Datum} contains object's state to save.
	 */
	public void save(long objectId, Datum[] data);

	/**
	 * Delete specified object based on its Id.
	 * 
	 * @param objectId
	 *            Object Id to delete from storage.
	 */
	public void remove(long objectId);

	/**
	 * Check for object existance based on its Id.
	 * 
	 * @param objectId
	 *            Object Id to search.
	 * @return <code>true</code> if specified object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(long objectId);

	/**
	 * Find and return all object Ids exist in the storage.
	 * 
	 * @return array of long contains object IDs.
	 */
	public long[] fetchAllIds();

	/**
	 * Get new and valid object Id. When the object Id is retrieved, it is
	 * recommended to immediately save the object to avoid collision with the
	 * next caller of this function.
	 * 
	 * @return available object Id on the storage.
	 */
	public long nextAvailableId();

	/**
	 * Build object with specified object Id.
	 * 
	 * @param objectId
	 *            Object Id to be built.
	 * @param factory
	 *            {@link ObjectFactory} to be delegated the job of the object
	 *            building.
	 * @return Object with specified Id.
	 */
	public Object build(long objectId, ObjectFactory factory);
}
