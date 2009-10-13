package id.web.herlangga.badulik.backend.rms;

import java.util.*;

import id.web.herlangga.badulik.*;
import id.web.herlangga.badulik.definition.*;

/**
 * Sometimes, in order to make impression that an {@link ObjectRepository} can
 * stores polymorphic object, we need several {@link ObjectRepository} to
 * support it. In that scenario, {@link RMSRepositoryGroupManager} will comes in
 * handy.
 * 
 * @author angga
 * 
 */
public class RMSRepositoryGroupManager implements ObjectRepositoryManager {
	private final String prefix;
	private final RMSRepositoryManager repositoryManager;
	private final Vector openedRepositoriesLog = new Vector();

	public RMSRepositoryGroupManager(String prefix,
			RMSRepositoryManager repositoryManager) {
		this.prefix = prefix;
		this.repositoryManager = repositoryManager;
	}

	public ObjectRepository get(String name, Schema objectSchema) {
		String repositoryName = prefix + name;
		ObjectRepository repository = repositoryManager.get(repositoryName,
				objectSchema);
		appendToLog(repositoryName);

		return repository;
	}

	public void drop(String name) {
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
