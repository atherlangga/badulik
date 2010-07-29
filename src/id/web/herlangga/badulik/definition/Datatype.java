package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Define supported {@link Datatype} for persisting Object.
 * 
 * @author angga
 * 
 */
public class Datatype {
	private final byte type;

	private Datatype(byte type) {
		this.type = type;
	}

	public static Datatype of(byte type) {
		return (Datatype) MAPPING.get(new Byte(type));
	}

	public byte typeAsByte() {
		return type;
	}

	public static final Datatype INT = new Datatype((byte) 1) {
		public String toString() {
			return "Int";
		}
	};
	public static final Datatype LONG = new Datatype((byte) 2) {
		public String toString() {
			return "Long";
		}
	};
	public static final Datatype STRING = new Datatype((byte) 3) {
		public String toString() {
			return "String";
		}
	};
	public static final Datatype DATE = new Datatype((byte) 4) {
		public String toString() {
			return "Date";
		}
	};
	public static final Datatype BOOL = new Datatype((byte) 5) {
		public String toString() {
			return "Bool";
		}
	};

	private static final Hashtable MAPPING = createMapping();

	private static Hashtable createMapping() {
		Hashtable mapping = new Hashtable();
		mapping.put(new Byte((byte) 1), INT);
		mapping.put(new Byte((byte) 2), LONG);
		mapping.put(new Byte((byte) 3), STRING);
		mapping.put(new Byte((byte) 4), DATE);
		mapping.put(new Byte((byte) 5), BOOL);

		return mapping;
	}

}
