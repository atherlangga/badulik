package id.web.herlangga.badulik;

/**
 * Domain Object creator.
 * 
 * @author angga
 * 
 */
public interface DomainObjectFactory {
	/**
	 * Create domain object with specified ID.
	 * 
	 * @param domainObjectID
	 *            Unique Domain Object ID.
	 * @param data
	 *            Data for Domain Object.
	 * @return Domain Object.
	 */
	public Object createDomainObject(long domainObjectID,
			AttributeValuePair[] data);
}
