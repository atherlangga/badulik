package id.web.herlangga.badulik;

import javax.microedition.rms.RecordFilter;

/**
 * Matcher to be used when RMS needs to translate from Domain ID to RMS
 * Record ID.
 * 
 * @author angga
 * 
 */
public interface RMSDomainIDMatcher {
	public RecordFilter getRecordFilterMatcherFor(long domainObjectID);
}
