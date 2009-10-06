package id.web.herlangga.badulik;

/**
 * {@link ObjectFilter} to help {@link ObjectRepository} to filter its
 * collections.
 * 
 * @author angga
 * 
 */
public interface ObjectFilter {

	/**
	 * Check whether some Object matches desired specification.
	 * 
	 * @param object
	 *            Object to check.
	 * @return <code>true</code> if the Object matches specification, else
	 *         <code>false</code>.
	 */
	public boolean matches(Object object);
}
