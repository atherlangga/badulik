package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.AttributeValuePair;
import id.web.herlangga.badulik.definition.DataType;
import id.web.herlangga.badulik.definition.Structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

import jmunit.framework.cldc11.TestCase;

public class RepositoryHelperRMSTest extends TestCase {
	private static final String storageName = "test";
	private RepositoryHelperRMS helper;

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests
	 *            the total of test methods present in the class.
	 * @param name
	 *            this testcase's name.
	 */
	public RepositoryHelperRMSTest() {
		super(5, "RepositoryHelperRMSTest");
	}

	public void recordStoreWarmUp() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);

			dos.writeInt(5);
			dos.writeUTF("Five");

			int id = RMSRecordStoresManager
					.recordStoreFor(storageName)
					.addRecord(baos.toByteArray(), 0, baos.toByteArray().length);

			byte[] raw = RMSRecordStoresManager.recordStoreFor(storageName)
					.getRecord(id);
			ByteArrayInputStream bais = new ByteArrayInputStream(raw);
			DataInputStream dis = new DataInputStream(bais);

			assertEquals("First reading should be equals to first writing", dis
					.readInt(), 5);
			assertEquals("Second reading should be equals to second writing",
					dis.readUTF(), "Five");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}

	}

	public void simpleFindRecordTest() {
		Structure structure = new Structure();
		structure.add("id", DataType.INT);
		structure.add("content", DataType.STRING);

		AttributeValuePair[] data = new AttributeValuePair[2];
		data[0] = new AttributeValuePair(structure.getAttributeNumber(0),
				new Integer(10));
		data[1] = new AttributeValuePair(structure.getAttributeNumber(1), "Ten");

		int id = helper.nextAvailableDomainObjectID();
		helper.saveRecord(id, data);

		AttributeValuePair[] fromRepository = helper.findRecord(id, structure);
		assertEquals("First value before and after persisted should be equals",
				data[0].getValue(), fromRepository[0].getValue());
		assertEquals(
				"Second value before and after persisted should be equals",
				data[1].getValue(), fromRepository[1].getValue());
	}

	public void removeRecordTest() {
		fail("Not Yet Implemented.");
	}

	public void saveRecordTest() {
		fail("Not Yet Implemented.");
	}

	public void findAllDomainObjectIDsTest() {
		fail("Not Yet Implemented.");
	}

	public void buildDomainObjectTest() {
		fail("Not Yet Implemented.");
	}

	/**
	 * A empty method used by the framework to initialize the tests. If there's
	 * 5 test methods, the setUp is called 5 times, one for each method. The
	 * setUp occurs before the method's execution, so the developer can use it
	 * to any necessary initialization. It's necessary to override it, however.
	 * 
	 * @throws Throwable
	 *             anything that the initialization can throw.
	 */
	public void setUp() throws Throwable {
		helper = new RepositoryHelperRMS(storageName);
	}

	/**
	 * A empty mehod used by the framework to release resources used by the
	 * tests. If there's 5 test methods, the tearDown is called 5 times, one for
	 * each method. The tearDown occurs after the method's execution, so the
	 * developer can use it to close something used in the test, like a
	 * nputStream or the RMS. It's necessary to override it, however.
	 */
	public void tearDown() {
		RMSRecordStoresManager.deleteRecordStore(storageName);
	}

	/**
	 * This method stores all the test methods invocation. The developer must
	 * implement this method with a switch-case. The cases must start from 0 and
	 * increase in steps of one until the number declared as the total of tests
	 * in the constructor, exclusive. For example, if the total is 3, the cases
	 * must be 0, 1 and 2. In each case, there must be a test method invocation.
	 * 
	 * @param testNumber
	 *            the test to be executed.
	 * @throws Throwable
	 *             anything that the executed test can throw.
	 */
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			recordStoreWarmUp();
			break;
		case 1:
			simpleFindRecordTest();
			break;
		case 2:
			removeRecordTest();
			break;
		case 3:
			saveRecordTest();
			break;
		case 4:
			findAllDomainObjectIDsTest();
			break;
		}
	}

}
