package id.web.herlangga.badulik;

/**
 * Provides common functionality for Repository.
 * 
 * @author angga
 * 
 */
public interface RepositoryHelper {
	public AttributeValuePair[] findRecord(int domainObjectID,
			Structure objectStructure);

	public void saveRecord(int domainObjectID, AttributeValuePair[] data);

	public void removeRecord(int domainObjectID);
	
	public int[] findAllDomainObjectIDs();

	public int nextAvailableDomainObjectID();
}
