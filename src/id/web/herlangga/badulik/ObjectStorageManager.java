package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Schema;

/**
 * Manager of {@link ObjectStorage}s.
 * 
 * It manages lifetime of several {@link ObjectStorage}s. If one needs to create
 * or delete {@link ObjectStorage}, he/she must use this object.
 * 
 * @author angga
 * 
 */
public interface ObjectStorageManager {

	/**
	 * Get {@link ObjectStorage} given unique name identifier and the Object
	 * {@link Schema}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectStorage}.
	 * @param objectSchema
	 *            {@link Schema} of the Domain Object kept in the
	 *            {@link ObjectStorage}.
	 * @return existing {@link ObjectStorage} with name and {@link Schema}
	 *         specified, or new {@link ObjectStorage} based on name and
	 *         {@link Schema} supplied.
	 */
	public ObjectStorage get(String name, Schema objectSchema);

	/**
	 * Remove specified {@link ObjectStorage}.
	 * 
	 * @param name
	 *            Unique identifier of the {@link ObjectStorage}.
	 */
	public void drop(String name);
}
