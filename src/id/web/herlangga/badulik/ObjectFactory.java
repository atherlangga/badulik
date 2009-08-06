package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.DataTypeAndValuePair;

/**
 * Domain Object creator.
 * 
 * @author angga
 * 
 */
public interface ObjectFactory {
	/**
	 * Create domain object with specified data.
	 * 
	 * @param data
	 *            Data for Domain Object.
	 * @return Domain Object.
	 */
	public Object createDomainObject(DataTypeAndValuePair[] data);
}
