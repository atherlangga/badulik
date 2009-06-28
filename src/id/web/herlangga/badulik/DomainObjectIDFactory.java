package id.web.herlangga.badulik;

import java.io.InputStream;

/**
 * Factory for Domain Object.
 * 
 * @author angga
 * 
 */
public interface DomainObjectIDFactory {
	/**
	 * Fetch Domain Object ID from specified {@link InputStream}.
	 * 
	 * @param input
	 *            Data contains ID to be read.
	 * @return Domain Object ID converted to {@link Long}.
	 */
	public long fetchDomainObjectIDFrom(InputStream input);
}
