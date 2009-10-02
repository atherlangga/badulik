package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

public class RepositoryManagerRMS implements RepositoryManager {
	private static final String ID_SUFFIX = "Id";
	private static final String STATE_SUFFIX = "State";

	public ObjectRepository get(String name, Structure objectStateStructure) {
		String objectIdRecordStoreName = name + ID_SUFFIX;
		String objectStateRecordStoreName = name + STATE_SUFFIX;

		return new ObjectRepositoryRMS(objectIdRecordStoreName,
				objectStateRecordStoreName, objectStateStructure);
	}

	public void drop(String name) {
		RecordStoresGateway.deleteRecordStore(name);
	}

}
