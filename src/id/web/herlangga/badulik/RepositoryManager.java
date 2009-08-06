package id.web.herlangga.badulik;

import id.web.herlangga.badulik.definition.Structure;

public interface RepositoryManager {
	public ObjectRepository getRepository(String name, Structure objectStructure);
	public void dropRepository(String name);
}
