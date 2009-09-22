package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

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
	 *            array of {@link Datum} contains object's states.
	 * @return created object.
	 */
	public Object createObject(Datum[] data);
}
