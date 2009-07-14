package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.FieldValuePair;
import id.web.herlangga.badulik.definition.Structure;

/**
 * Provides common functionality for Repository.
 * 
 * @author angga
 * 
 */
public interface RepositoryHelper {
	public FieldValuePair[] findRecord(int domainObjectID, Structure fieldsList);

	public void saveRecord(int domainObjectID, FieldValuePair[] data);

	public void removeRecord(int domainObjectID);

	public int[] findAllDomainObjectIDs(Structure fieldsList);

	public int nextAvailableDomainObjectID();

	public Object buildDomainObject(int domainObjectID, Structure fieldsList,
			DomainObjectFactory factory);
}
