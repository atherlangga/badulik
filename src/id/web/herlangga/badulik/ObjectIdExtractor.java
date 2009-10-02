package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object ID extractor to help {@link ObjectRepository} persisting Object.
 * 
 * @author angga
 * 
 */
public interface ObjectIdExtractor {

	/**
	 * Extract ID from specified Object so {@link ObjectRepository} can differ
	 * one Object over another.
	 * 
	 * @param toExtract
	 *            Object to extract ID from.
	 * @return extracted Object ID.
	 */
	public Element extractIdFrom(Object toExtract);
}
