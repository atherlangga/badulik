package id.web.herlangga.badulik;

import id.web.herlangga.badulik.backend.rms.RepositoryHelperRMS;
import id.web.herlangga.badulik.definition.Structure;

public final class BadulikService {
	private static Backend backend;

	public static void setBackend(Backend backend) {
		BadulikService.backend = backend;
	}

	public static RepositoryHelper getRepositoryHelperFor(
			String repositoryName, Structure domainObjectStructure) {
		if (BadulikService.backend == Backend.RMS) {
			return new RepositoryHelperRMS(repositoryName,
					domainObjectStructure);
		}

		throw new IllegalStateException("Invalid selected Backend.");
	}

}
