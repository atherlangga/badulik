package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

public class RepositoryManagerRMS implements RepositoryManager {

	public ObjectRepository get(String name, Structure objectStructure) {
		return new ObjectRepositoryRMS(name, objectStructure);
	}

	public void drop(String name) {
		RecordStoresGateway.deleteRecordStore(name);
	}

}
