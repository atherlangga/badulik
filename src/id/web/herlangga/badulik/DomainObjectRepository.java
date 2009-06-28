package id.web.herlangga.badulik;

/**
 * Repository for Domain Object.
 * 
 * @author angga
 * 
 */
public interface DomainObjectRepository {
	/**
	 * The name of Repository. It is REQUIRED that this name to be unique within
	 * application scope. To check whether particular name is registered or not,
	 * use method RMSRecordStoresManager.isExist().
	 * 
	 * @return Unique name of the Repository.
	 * @see RMSRecordStoresManager.
	 */
	public String getName();

	/**
	 * Get {@link Structure} of the Domain Object to be kept in this Repository.
	 * 
	 * @return Domain Object {@link Structure}.
	 */
	public Structure getStructure();
}
