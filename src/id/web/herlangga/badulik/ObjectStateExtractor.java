package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object state extractor to help {@link ObjectRepository} in attemp to
 * persisting Object.
 * 
 * @author angga
 * 
 */
public interface ObjectStateExtractor {

	/**
	 * Extract object's state so it can persisted and reconstituted.
	 * 
	 * @param toExtract
	 *            Object which state is about to be extracted.
	 * @return extracted data which is represented by array of {@link Element}.
	 */
	public Element[] extractStateFrom(Object toExtract);
}
