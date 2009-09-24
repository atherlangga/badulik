package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object reconstitutor.
 * 
 * @author angga
 * 
 */
public interface ObjectReconstitutor {
	/**
	 * Reconstitute object with specified data.
	 * 
	 * @param states
	 *            Array of {@link Datum} contains object's states.
	 * @return Reconstituted object.
	 */
	public Object reconstituteObjectFrom(Datum[] states);
}
