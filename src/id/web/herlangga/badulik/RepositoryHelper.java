package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.AttributeValuePair;
import id.web.herlangga.badulik.definition.Structure;

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

	public Object buildDomainObject(int domainObjectID,
			Structure objectStructure, DomainObjectFactory factory);
}
