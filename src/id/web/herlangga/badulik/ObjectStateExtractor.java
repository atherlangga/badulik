package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.*;

/**
 * Object state extractor to assist {@link ObjectRepository} in order to
 * persisting Object state.
 * 
 * @author angga
 * 
 */
public interface ObjectStateExtractor {

	/**
	 * Extract Object's state so it can persisted and reconstituted.
	 * 
	 * @param toExtract
	 *            Object which state is about to be extracted.
	 * @return extracted state data represented in {@link Tuple}.
	 */
	public Tuple extractStateFrom(Object toExtract);
}
