package id.web.herlangga.badulik;

import id.web.herlangga.badulik.backend.rms.RMSRecordStoresManager;
import id.web.herlangga.badulik.backend.rms.RepositoryHelperRMS;

public final class ServiceLocator {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		ServiceLocator.backend = backend;
	}

	public static RepositoryHelper getRepositoryHelperFor(String repositoryName) {
		if (ServiceLocator.backend == Backend.RMS) {
			return new RepositoryHelperRMS(repositoryName);
		}
		
		throw new IllegalStateException("Invalid selected Backend.");
	}
	
	public static void drop(String repositoryName) {
		RMSRecordStoresManager.deleteRecordStore(repositoryName);
	}
}
