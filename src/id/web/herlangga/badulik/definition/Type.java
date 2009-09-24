package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Define supported types for persisting object's states.
 * 
 * @author angga
 * 
 */
public class Type {
	private final byte type;

	private Type(byte type) {
		this.type = type;
	}

	public static Type of(byte type) {
		return (Type) typeMapping().get(new Byte(type));
	}

	public byte typeAsByte() {
		return type;
	}
	
	private static final Hashtable typeMapping() {
		Hashtable typeMapping = new Hashtable();
		typeMapping.put(new Byte((byte) 1), INT);
		typeMapping.put(new Byte((byte) 2), LONG);
		typeMapping.put(new Byte((byte) 3), STRING);
		typeMapping.put(new Byte((byte) 4), DATE);
		typeMapping.put(new Byte((byte) 5), BOOL);
		
		return typeMapping;
	}

	public static final Type INT = new Type((byte) 1);
	public static final Type LONG = new Type((byte) 2);
	public static final Type STRING = new Type((byte) 3);
	public static final Type DATE = new Type((byte) 4);
	public static final Type BOOL = new Type((byte) 5);

}
