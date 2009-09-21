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
	 * Get {@link ObjectRepository} given its name and the {@link Structure} of
	 * the Domain Object kept.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 * @param objectStructure
	 *            {@link Structure} of the Domain Object kept in the
	 *            {@link ObjectRepository}.
	 * @return Existing {@link ObjectRepository} with name and {@link Structure}
	 *         specified, or new {@link ObjectRepository} based on name and
	 *         {@link Structure} supplied.
	 */
	public ObjectRepository getRepository(String name, Structure objectStructure);

	/**
	 * Remove {@link ObjectRepository} on all of kept Domain Object.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectRepository}.
	 */
	public void dropRepository(String name);
}
