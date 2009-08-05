package id.web.herlangga.badulik;

import id.web.herlangga.badulik.backend.rms.RepositoryWorkerRMS;
import id.web.herlangga.badulik.definition.Structure;

public final class BadulikService {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		BadulikService.backend = backend;
	}

	public static RepositoryWorker createWorkerFor(String repositoryName,
			Structure domainObjectStructure) {
		if (BadulikService.backend == Backend.RMS) {
			return new RepositoryWorkerRMS(repositoryName,
					domainObjectStructure);
		}

		throw new IllegalStateException("Invalid selected Backend.");
	}

}
