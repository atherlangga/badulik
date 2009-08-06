package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.DataTypeAndValuePair;

/**
 * Domain Object Repository.
 * 
 * @author angga
 * 
 */
public interface ObjectRepository {
	/**
	 * Fetch data from specified Domain Object ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID whose data is needed.
	 * @return array of {@link DataTypeAndValuePair} contains data for specified
	 *         Domain Object ID.
	 */
	public DataTypeAndValuePair[] find(long domainObjectID);

	/**
	 * Store Domain Object with specified data. Based on the Domain Object ID,
	 * it will make decision whether to insert new record or edit existing
	 * record.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID.
	 * @param data
	 *            array of {@link DataTypeAndValuePair} contains data for the
	 *            specified Domain Object.
	 */
	public void save(long domainObjectID, DataTypeAndValuePair[] data);

	/**
	 * Delete specified Domain Object based on its ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to be deleted from storage.
	 */
	public void remove(long domainObjectID);

	/**
	 * Check whether specified Domain Object is exist in the storage or not.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to identify every unique Domain Object.
	 * @return <code>true</code> if specified Domain Object ID exist, else
	 *         <code>false</code>.
	 */
	public boolean isExist(long domainObjectID);

	/**
	 * Find and return all Domain Object IDs exist in the storage.
	 * 
	 * @return array of long contains Domain Object IDs.
	 */
	public long[] fetchAllIDs();

	/**
	 * Get available ID, useful when generating new Domain Object ID. However,
	 * when the ID is retrieved, it is recommended to immediately save the
	 * Domain Object to avoid collision with next caller of this function.
	 * 
	 * @return available ID on the storage.
	 */
	public long nextAvailableID();

	/**
	 * Build Domain Object with specified Domain Object ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to be built.
	 * @param factory
	 *            {@link ObjectFactory} to be delegated the job of the Domain
	 *            Object building.
	 * @return Domain Object with specified Domain Object ID.
	 */
	public Object build(long domainObjectID, ObjectFactory factory);
}
