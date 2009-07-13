package id.web.herlangga.badulik;

import id.web.herlangga.badulik.backend.rms.RMSRecordStoresManager;
import id.web.herlangga.badulik.backend.rms.RepositoryHelperRMS;

import javax.microedition.rms.RecordStore;

public final class ServiceLocator {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		ServiceLocator.backend = backend;
	}

	public static RepositoryHelper getRepositoryHelperFor(String repositoryName) {
		if (ServiceLocator.backend == Backend.RMS) {
			RecordStore storage = RMSRecordStoresManager.recordStoreFor(repositoryName);
			return new RepositoryHelperRMS(storage);
		}
		
		throw new IllegalStateException("Invalid selected Backend.");
	}
	
	public static void drop(String repositoryName) {
		RMSRecordStoresManager.recordStoreFor(repositoryName);
	}
}
