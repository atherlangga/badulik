package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Structure;

/**
 * Object repositories manager.
 * 
 * @author angga
 * 
 */
public interface RepositoryManager {

	/**
	 * Get {@link ObjectRepository} given unique name identifier and the Object
	 * {@link Structure}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 * @param objectStructure
	 *            {@link Structure} of the Domain Object kept in the
	 *            {@link ObjectRepository}.
	 * @return existing {@link ObjectRepository} with name and {@link Structure}
	 *         specified, or new {@link ObjectRepository} based on name and
	 *         {@link Structure} supplied.
	 */
	public ObjectRepository get(String name, Structure objectStructure);

	/**
	 * Remove specified {@link ObjectRepository}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 */
	public void drop(String name);
}
