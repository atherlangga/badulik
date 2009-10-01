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
	 * Find object with specified ID.
	 * 
	 * @param objectId
	 *            {@link Datum} object ID.
	 * @param reconstitutor
	 *            {@link ObjectReconstitutor} to be delegated the job of the
	 *            object reconstitution.
	 * @return Object with specified ID.
	 */
	public Object find(Datum objectId, ObjectReconstitutor reconstitutor);

	/**
	 * Store object. Based on the extracted object ID, {@link ObjectRepository}
	 * will make decision whether to insert new record or edit existing record.
	 * 
	 * @param object
	 *            {@link Object} to store.
	 * @param extractor
	 *            {@link ObjectStatesExtractor} to extract object's state.
	 */
	public void save(Object object, ObjectStatesExtractor extractor);

	/**
	 * Delete specified object based on its ID.
	 * 
	 * @param objectId
	 *            Object ID to delete from storage.
	 */
	public void remove(Datum objectId);

	/**
	 * Check for object existance by its ID.
	 * 
	 * @param objectId
	 *            Object ID to search.
	 * @return <code>true</code> if specified object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(Datum objectId);

	/**
	 * Find and return all object IDs exist in the storage.
	 * 
	 * @return array of long contains object IDs.
	 */
	public Datum[] fetchAllIds();

	/**
	 * Generate valid sequence value. It's intended helping to create Object ID.
	 * 
	 * @return sequence value in the form of long intended to be used for Object
	 *         ID.
	 */
	public long generateSequenceValue();

}
