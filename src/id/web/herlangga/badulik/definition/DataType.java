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

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataType other = (DataType) obj;
		if (type != other.type)
			return false;
		return true;
	}
	
	public static DataType fromInteger(int integerValue) {
		return new DataType(integerValue);
	}
	
	public int toInteger() {
		return type;
	}
	
}
