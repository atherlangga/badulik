package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

public class RMSStorageManager implements ObjectStorageManager {
	public ObjectStorage get(String name, Schema objectSchema) {
		String objectIdRecordStoreName = name + ID_SUFFIX;
		String objectStateRecordStoreName = name + STATE_SUFFIX;

		return new RMSStorage(objectIdRecordStoreName,
				objectStateRecordStoreName, objectSchema);
	}

	public void drop(String name) {
		RecordStoresGateway.deleteRecordStore(name + ID_SUFFIX);
		RecordStoresGateway.deleteRecordStore(name + STATE_SUFFIX);
	}

	private static final String ID_SUFFIX = "Id";
	private static final String STATE_SUFFIX = "State";
}
