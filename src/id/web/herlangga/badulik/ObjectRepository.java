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
	 * Find object with specified Id.
	 * 
	 * @param objectId
	 *            {@link Datum} object Id.
	 * @param reconstitutor
	 *            {@link ObjectReconstitutor} to be delegated the job of the
	 *            object building.
	 * @return Object with specified Id.
	 */
	public Object find(Datum objectId, ObjectReconstitutor reconstitutor);

	/**
	 * Store object. Based on the object Id, {@link ObjectRepository} will make
	 * decision whether to insert new record or edit existing record.
	 * 
	 * @param object
	 *            {@link Object} to store.
	 * @param extractor
	 *            {@link ObjectStateExtractor} to extract object's state.
	 */
	public void save(Object object, ObjectStateExtractor extractor);

	/**
	 * Delete specified object based on its Id.
	 * 
	 * @param objectId
	 *            Object Id to delete from storage.
	 */
	public void remove(Datum objectId);

	/**
	 * Check for object existance based on its Id.
	 * 
	 * @param objectId
	 *            Object Id to search.
	 * @return <code>true</code> if specified object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(Datum objectId);

	/**
	 * Find and return all object Ids exist in the storage.
	 * 
	 * @return array of long contains object IDs.
	 */
	public Datum[] fetchAllIds();

}
