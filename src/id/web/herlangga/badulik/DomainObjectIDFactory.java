package id.web.herlangga.badulik;

import java.io.InputStream;

/**
 * Factory for Domain Object.
 * 
 * @author angga
 * 
 */
public interface DomainObjectIDFactory {
	public long fetchDomainObjectIDFrom(InputStream input);
}

