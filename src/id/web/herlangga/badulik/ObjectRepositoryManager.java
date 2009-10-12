package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Schema;

/**
 * Manages lifetime of several {@link ObjectRepository}s.
 * 
 * @author angga
 * 
 */
public interface ObjectRepositoryManager {

	/**
	 * Get {@link ObjectRepository} given unique name identifier and the Object
	 * {@link Schema}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 * @param objectSchema
	 *            {@link Schema} of the Domain Object kept in the
	 *            {@link ObjectRepository}.
	 * @return existing {@link ObjectRepository} with name and {@link Schema}
	 *         specified, or new {@link ObjectRepository} based on name and
	 *         {@link Schema} supplied.
	 */
	public ObjectRepository get(String name, Schema objectSchema);

	/**
	 * Remove specified {@link ObjectRepository}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 */
	public void drop(String name);
}
