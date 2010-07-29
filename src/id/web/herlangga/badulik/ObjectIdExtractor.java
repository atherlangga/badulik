package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object ID extractor to help {@link ObjectStorage} extract Object ID of
 * certain Object in order to persist Object.
 * 
 * @author angga
 * 
 */
public interface ObjectIdExtractor {

	/**
	 * Extract ID from specified Object so {@link ObjectStorage} can differ
	 * one Object from another in its collections.
	 * 
	 * @param toExtract
	 *            Object to extract ID from.
	 * @return extracted Object ID.
	 */
	public Element extractIdFrom(Object toExtract);
}
