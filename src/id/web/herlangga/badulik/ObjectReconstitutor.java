package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object reconstitutor for helping {@link ObjectRepository} reconstitute
 * Object.
 * 
 * @author angga
 * 
 */
public interface ObjectReconstitutor {

	/**
	 * Reconstitute object with specified data.
	 * 
	 * @param objecId
	 *            Object ID.
	 * @param state
	 *            Array of {@link Element} to represents Object state.
	 * @return reconstituted Object.
	 */
	public Object reconstituteObjectWith(Element objecId, Element[] state);
}
