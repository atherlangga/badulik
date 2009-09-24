package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object state extractor to extract object's state.
 * 
 * @author angga
 * 
 */
public interface ObjectStateExtractor {

	/**
	 * Extract Object's state so it can persisted and reconstituted back.
	 * 
	 * @param toExtract
	 *            {@link Object} which state is about to extracted.
	 * @return extracted data which is represented by array of {@link Datum}.
	 */
	public Datum[] extractStateFrom(Object toExtract);
}
