package id.web.herlangga.badulik;


public class Type {
	public static final Type INT = new Type(1);
	public static final Type LONG = new Type(2);
	public static final Type STRING = new Type(3);
	public static final Type DATE = new Type(4);
	public static final Type BOOL = new Type(5);
	
	private int type;
	
	protected Type(int type) {
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
		Type other = (Type) obj;
		if (type != other.type)
			return false;
		return true;
	}
	
	public static Type fromInteger(int integerValue) {
		return new Type(integerValue);
	}
	
	public int toInteger() {
		return type;
	}
	
}
