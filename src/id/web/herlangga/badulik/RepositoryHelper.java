package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.FieldValuePair;

/**
 * Provides common functionality for Repository.
 * 
 * @author angga
 * 
 */
public interface RepositoryHelper {
	public FieldValuePair[] findRecord(int domainObjectID);

	public void saveRecord(int domainObjectID, FieldValuePair[] data);

	public void removeRecord(int domainObjectID);

	public int[] findAllDomainObjectIDs();

	public int nextAvailableDomainObjectID();

	public Object buildDomainObject(int domainObjectID,
			DomainObjectFactory factory);
}
