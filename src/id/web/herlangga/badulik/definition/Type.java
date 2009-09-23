package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Define supported types for persisting object's states.
 * 
 * @author angga
 * 
 */
public class Type {
	private final int type;

	protected Type(int type) {
		this.type = type;
	}

	public static Type of(int type) {
		return (Type) typeMapping().get(new Integer(type));
	}

	public int typeAsInteger() {
		return type;
	}
	
	private static final Hashtable typeMapping() {
		Hashtable typeMapping = new Hashtable();
		typeMapping.put(new Integer(1), INT);
		typeMapping.put(new Integer(2), LONG);
		typeMapping.put(new Integer(3), STRING);
		typeMapping.put(new Integer(4), DATE);
		typeMapping.put(new Integer(5), BOOL);
		
		return typeMapping;
	}

	public static final Type INT = new Type(1);
	public static final Type LONG = new Type(2);
	public static final Type STRING = new Type(3);
	public static final Type DATE = new Type(4);
	public static final Type BOOL = new Type(5);

}
