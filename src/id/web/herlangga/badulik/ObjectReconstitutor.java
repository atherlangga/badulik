package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object reconstitutor to reconstitute saved Object in the
 * {@link ObjectRepository}.
 * 
 * @author angga
 * 
 */
public interface ObjectReconstitutor {

	/**
	 * Reconstitute object with specified data.
	 * 
	 * @param objectId
	 *            Object ID.
	 * @param state
	 *            {@link Tuple} to represents Object state.
	 * @return reconstituted Object.
	 */
	public Object reconstituteObjectWith(Element objectId, Tuple state);
}
