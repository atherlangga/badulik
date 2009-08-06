package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.ObjectRepository;
import id.web.herlangga.badulik.RepositoryManager;
import id.web.herlangga.badulik.definition.Structure;

public class RepositoryManagerRMS implements RepositoryManager {

	public ObjectRepository getRepository(String name, Structure objectStructure) {
		return new ObjectRepositoryRMS(name, objectStructure);
	}

	public void dropRepository(String name) {
		RecordStoresGateway.deleteRecordStore(name);
	}

}
