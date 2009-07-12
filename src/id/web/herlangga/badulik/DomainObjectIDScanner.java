package id.web.herlangga.badulik;

import java.io.InputStream;

/**
 * Object that recognize Domain Object ID in an {@link InputStream}.
 * 
 * @author angga
 * 
 */
public interface DomainObjectIDScanner {
	/**
	 * Fetch Domain Object ID from specified {@link InputStream}.
	 * 
	 * @param input
	 *            Data contains ID to be read.
	 * @return Domain Object ID converted to {@link Long}.
	 */
	public long fetchDomainObjectIDFrom(InputStream input);
}
