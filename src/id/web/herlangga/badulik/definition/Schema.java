package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Object definition.
 * 
 * @author angga
 * 
 */
public class Schema {
	private final Attribute[] attributes;

	private Schema(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public static SchemaBuilder buildNew() {
		return new SchemaBuilder();
	}

	public String attributeNameAt(int attributeNumber) {
		return attributes[attributeNumber].name();
	}

	public int attributesSize() {
		return attributes.length;
	}

	public boolean isCompatibleWith(Element[] elements) {
		if (hasDifferentSizeWith(elements)) {
			return false;
		}

		int dataLength = elements.length;
		for (int i = 0; i < dataLength; i++) {
			if (attributes[i].isIncompatibleWith(elements[i])) {
				return false;
			}
		}

		return true;
	}

	public boolean isIncompatibleWith(Element[] data) {
		return !isCompatibleWith(data);
	}

	int attributeNumberOf(String attributeName) {
		int fieldLength = attributes.length;
		for (int fieldNumber = 0; fieldNumber < fieldLength; fieldNumber++) {
			Attribute currentField = attributes[fieldNumber];
			if (currentField.name().equals(attributeName)) {
				return fieldNumber;
			}
		}

		throw new IllegalArgumentException(attributeName + " is not exist.");
	}

	Attribute attributeOf(String attributeName) {
		return attributes[attributeNumberOf(attributeName)];
	}

	private boolean hasSameSizeWith(Element[] elements) {
		return attributes.length == elements.length;
	}

	private boolean hasDifferentSizeWith(Element[] elements) {
		return !hasSameSizeWith(elements);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Schema.hashCode(attributes);
		return result;
	}

	private static int hashCode(Object[] array) {
		int prime = 31;
		if (array == null) {
			return 0;
		}
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = prime * result
					+ (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Schema other = (Schema) obj;
		int fieldsLength = attributes.length;
		for (int i = 0; i < fieldsLength; i++) {
			if (!attributes[i].equals(other.attributes[i])) {
				return false;
			}
		}
		return true;
	}

	public static class SchemaBuilder {
		private Vector names;
		private Vector types;

		private SchemaBuilder() {
			this.names = new Vector();
			this.types = new Vector();
		}

		public SchemaBuilder thenAddAttribute(String attributeName,
				Datatype attributeType) {
			if (names.contains(attributeName)) {
				throw new IllegalArgumentException(attributeName
						+ " already exists.");
			}
			names.addElement(attributeName);
			types.addElement(attributeType);
			return this;
		}

		public Schema thenGetResult() {
			int attributesSize = names.size();
			Attribute[] fields = new Attribute[attributesSize];
			for (int i = 0; i < attributesSize; i++) {
				String attributeName = (String) names.elementAt(i);
				Datatype attributeType = (Datatype) types.elementAt(i);

				fields[i] = Attribute.of(attributeName, attributeType);
			}

			return new Schema(fields);
		}
	}

}
