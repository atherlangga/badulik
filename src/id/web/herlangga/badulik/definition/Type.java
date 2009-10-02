package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Define supported {@link Type} for persisting Object.
 * 
 * @author angga
 * 
 */
public class Type {
	private static Hashtable mapping;
	private final byte type;

	private Type(byte type) {
		this.type = type;
	}

	public static Type of(byte type) {
		if (mapping == null) {
			mapping = createMapping();
		}
		return (Type) mapping.get(new Byte(type));
	}

	public byte typeAsByte() {
		return type;
	}
	
	private static final Hashtable createMapping() {
		Hashtable mapping = new Hashtable();
		mapping.put(new Byte((byte) 1), INT);
		mapping.put(new Byte((byte) 2), LONG);
		mapping.put(new Byte((byte) 3), STRING);
		mapping.put(new Byte((byte) 4), DATE);
		mapping.put(new Byte((byte) 5), BOOL);
		
		return mapping;
	}

	public static final Type INT = new Type((byte) 1);
	public static final Type LONG = new Type((byte) 2);
	public static final Type STRING = new Type((byte) 3);
	public static final Type DATE = new Type((byte) 4);
	public static final Type BOOL = new Type((byte) 5);

}
