package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.DataTypeAndValuePair;

/**
 * Object creator.
 * 
 * @author angga
 * 
 */
public interface ObjectFactory {
	/**
	 * Create object with specified data.
	 * 
	 * @param data
	 *            Data for object.
	 * @return created object.
	 */
	public Object createDomainObject(DataTypeAndValuePair[] data);
}
