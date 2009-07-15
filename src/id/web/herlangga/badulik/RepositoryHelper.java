package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.DataTypeAndValuePair;

/**
 * Provides common functionality for Repository. To work properly, it needs to
 * supplied with the Structure of the Domain Object.
 * 
 * @author angga
 * 
 */
public interface RepositoryHelper {
	/**
	 * Fetch data from specified Domain Object ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID whose data is needed.
	 * @return array of {@link DataTypeAndValuePair} contains data for specified
	 *         Domain Object ID.
	 */
	public DataTypeAndValuePair[] findRecord(long domainObjectID);

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
	public void saveRecord(long domainObjectID, DataTypeAndValuePair[] data);

	/**
	 * Delete specified Domain Object based on its ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to be deleted from storage.
	 */
	public void removeRecord(long domainObjectID);

	/**
	 * Find and return all Domain Object IDs exist in the storage.
	 * 
	 * @return array of int contains Domain Object IDs.
	 */
	public long[] findAllDomainObjectIDs();

	/**
	 * Get available ID, useful when generating new Domain Object ID. However,
	 * when the ID is retrieved, it is recommended to immediately save the
	 * Domain Object to avoid collision with next caller of this function.
	 * 
	 * @return available ID on the storage.
	 */
	public int nextAvailableDomainObjectID();

	/**
	 * Build Domain Object with specified Domain Object ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID to be built.
	 * @param factory
	 *            {@link DomainObjectFactory} to be delegated the job of the
	 *            building.
	 * @return Domain Object with specified Domain Object ID.
	 */
	public Object buildDomainObject(long domainObjectID,
			DomainObjectFactory factory);
}
