package id.web.herlangga.badulik.backend.rms;

import java.util.*;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

/**
 * In order to make impression that an {@link ObjectStorage} behave like
 * in-memory {@link ObjectStorage} thus can stores polymorphic object, we
 * need several {@link ObjectStorage} to support it. In such scenario,
 * {@link RMSRepositoryGroupManager} will comes in handy. </p>
 * 
 * {@link RMSRepositoryGroupManager} will create several
 * {@link ObjectStorage}s with name prefixed to achieve uniquely named
 * {@link ObjectStorage}. Thus, it have two drop methods, a drop method to
 * drop specific {@link ObjectStorage}, and a dropAll method to drop all
 * {@link ObjectStorage}s created by this Object. </p>
 * 
 * @author angga
 * 
 */
public class RMSRepositoryGroupManager implements ObjectStorageManager {
	private final String prefix;
	private final RMSRepositoryManager repositoryManager;
	private final Vector openedRepositoriesLog = new Vector();

	public RMSRepositoryGroupManager(String prefix,
			RMSRepositoryManager repositoryManager) {
		this.prefix = prefix;
		this.repositoryManager = repositoryManager;
	}

	public ObjectStorage get(String name, Schema objectSchema) {
		String repositoryName = prefix + name;
		ObjectStorage repository = repositoryManager.get(repositoryName,
				objectSchema);
		appendToLog(repositoryName);

		return repository;
	}

	public void drop(String name) {
		String completeRepositoryName = prefix + name;
		repositoryManager.drop(completeRepositoryName);
		openedRepositoriesLog.removeElement(completeRepositoryName);
	}

	/**
	 * Drop all {@link ObjectStorage}s created by this Object.
	 */
	public void dropAll() {
		dropRepositoryGroupBasedOnLog();
		resetLog();
	}

	private void appendToLog(String repositoryName) {
		if (!openedRepositoriesLog.contains(repositoryName)) {
			openedRepositoriesLog.addElement(repositoryName);
		}
	}

	private void dropRepositoryGroupBasedOnLog() {
		Enumeration repositoryNamesEnumeration = openedRepositoriesLog
				.elements();
		while (repositoryNamesEnumeration.hasMoreElements()) {
			String repositoryName = (String) repositoryNamesEnumeration
					.nextElement();
			repositoryManager.drop(repositoryName);
		}
	}

	private void resetLog() {
		openedRepositoriesLog.removeAllElements();
	}

}
