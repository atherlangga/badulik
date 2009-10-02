package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Element;

/**
 * Object repository.
 * 
 * @author angga
 * 
 */
public interface ObjectRepository {

	/**
	 * Find Object with specified ID.
	 * 
	 * @param objectId
	 *            Object ID represented as {@link Element}.
	 * @param reconstitutor
	 *            {@link ObjectReconstitutor} to be delegated the job of the
	 *            object reconstitution.
	 * @return Object with specified ID.
	 */
	public Object find(Element objectId, ObjectReconstitutor reconstitutor);

	/**
	 * Store object. Based on the extracted object ID, {@link ObjectRepository}
	 * will make decision whether to insert new record or edit existing record.
	 * 
	 * @param object
	 *            {@link Object} to store.
	 * @param idExtractor
	 *            {@link ObjectIdExtractor} to extract Object ID.
	 * @param stateExtractor
	 *            {@link ObjectStateExtractor} to extract Object state.
	 */
	public void save(Object object, ObjectIdExtractor idExtractor,
			ObjectStateExtractor stateExtractor);

	/**
	 * Delete specified Object based on its ID.
	 * 
	 * @param objectId
	 *            Object ID to delete from storage.
	 */
	public void remove(Element objectId);

	/**
	 * Check for Object existance by its ID.
	 * 
	 * @param objectId
	 *            Object ID to check.
	 * @return <code>true</code> if specified Object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(Element objectId);

	/**
	 * Find and return all Object IDs exist in the storage.
	 * 
	 * @return array of long contains object IDs.
	 */
	public Element[] fetchAllIds();

	/**
	 * Generate valid sequence value. It's intended helping to create Object ID.
	 * 
	 * @return sequence value represented as long.
	 */
	public long generateSequenceValue();

}
