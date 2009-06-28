package id.web.herlangga.badulik;

import javax.microedition.rms.RecordFilter;

/**
 * Matcher to be used when RMS needs to translate from Domain Object ID to RMS
 * Record ID.
 * 
 * @author angga
 * 
 */
public interface RMSDomainIDMatcher {
	/**
	 * Get {@link RecordFilter} to be used as a matcher from Domain Object ID to
	 * RMS Record ID.
	 * 
	 * @param domainObjectID
	 *            Domain Object ID in {@link Long} form.
	 * @return {@link RecordFilter} that has ability to recognize Domain Object
	 *         ID.
	 */
	public RecordFilter getRecordFilterMatcherFor(long domainObjectID);
}
