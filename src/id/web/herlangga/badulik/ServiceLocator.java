package id.web.herlangga.badulik;

import id.web.herlangga.badulik.rms.RMSRecordStoresManager;
import id.web.herlangga.badulik.rms.RepositoryHelperRMS;

import javax.microedition.rms.RecordStore;

public final class ServiceLocator {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		ServiceLocator.backend = backend;
	}

	public static RepositoryHelper getRepositoryHelperFor(String uniqueRepositoryName) {
		if (ServiceLocator.backend == Backend.RMS) {
			RecordStore storage = RMSRecordStoresManager.recordStoreFor(uniqueRepositoryName);
			return new RepositoryHelperRMS(storage);
		}
		
		throw new IllegalArgumentException();
	}
}
