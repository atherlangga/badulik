package id.web.herlangga.badulik.definition;

/**
 * Define supported types.
 * 
 * @author angga
 * 
 */
public class Type {
	private final int type;

	protected Type(int type) {
		this.type = type;
	}

	public static Type fromInteger(int integerValue) {
		return new Type(integerValue);
	}

	public int typeAsInteger() {
		return type;
	}
	
	public static final Type INT = new Type(1);
	public static final Type LONG = new Type(2);
	public static final Type STRING = new Type(3);
	public static final Type DATE = new Type(4);
	public static final Type BOOL = new Type(5);

}
