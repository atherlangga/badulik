package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

public class RMSRepositoryGroupManager implements ObjectRepositoryManager {
	private final String prefix;
	private final RMSRepositoryManager repositoryManager;

	public RMSRepositoryGroupManager(String prefix,
			RMSRepositoryManager repositoryManager) {
		this.prefix = prefix;
		this.repositoryManager = repositoryManager;
	}
	
	public ObjectRepository get(String name, Schema objectSchema) {
		return repositoryManager.get(prefix + name, objectSchema);
	}

	public void drop(String name) {
		repositoryManager.drop(name + prefix);
	}

}
