package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Smallest meaningful value.
 * 
 * @author angga
 * 
 */
public class Element {
	private final Datatype type;
	private final Object value;

	public static Element of(int value) {
		return new Element(Datatype.INT, new Integer(value));
	}

	public static Element of(long value) {
		return new Element(Datatype.LONG, new Long(value));
	}

	public static Element of(String value) {
		return new Element(Datatype.STRING, value);
	}

	public static Element of(Date value) {
		return new Element(Datatype.DATE, value);
	}

	public static Element of(boolean value) {
		return new Element(Datatype.BOOL, new Boolean(value));
	}

	private Element(Datatype type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Datatype type() {
		return type;
	}

	public Object value() {
		return value;
	}

	public int valueAsInt() {
		return ((Integer) value).intValue();
	}

	public long valueAsLong() {
		return ((Long) value).longValue();
	}

	public String valueAsString() {
		return (String) value;
	}

	public Date valueAsDate() {
		return (Date) value;
	}

	public boolean valueAsBoolean() {
		return ((Boolean) value).booleanValue();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
