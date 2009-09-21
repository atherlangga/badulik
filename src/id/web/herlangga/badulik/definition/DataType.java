package id.web.herlangga.badulik.definition;

public class DataType {
	public static final DataType INT = new DataType(1);
	public static final DataType LONG = new DataType(2);
	public static final DataType STRING = new DataType(3);
	public static final DataType DATE = new DataType(4);
	public static final DataType BOOL = new DataType(5);
	
	private final int type;
	
	protected DataType(int type) {
		this.type = type;
	}
	
	public static DataType fromInteger(int integerValue) {
		return new DataType(integerValue);
	}
	
	public int typeAsInteger() {
		return type;
	}
	
}
