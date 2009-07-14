package id.web.herlangga.badulik;

import id.web.herlangga.badulik.backend.rms.RMSRecordStoresManager;
import id.web.herlangga.badulik.backend.rms.RepositoryHelperRMS;

public final class BadulikService {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		BadulikService.backend = backend;
	}

	public static RepositoryHelper getRepositoryHelperFor(String repositoryName, 
			DomainObjectIDScanner idScanner) {
		if (BadulikService.backend == Backend.RMS) {
			return new RepositoryHelperRMS(repositoryName, idScanner);
		}
		
		throw new IllegalStateException("Invalid selected Backend.");
	}
	
	public static void drop(String repositoryName) {
		RMSRecordStoresManager.deleteRecordStore(repositoryName);
	}
}
