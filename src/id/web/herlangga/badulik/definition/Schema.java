package id.web.herlangga.badulik.definition;

import java.util.*;

/**
 * Object state definition.
 * 
 * @author angga
 * 
 */
public class Schema {
	private final Field[] fields;

	private Schema(Field[] fields) {
		this.fields = fields;
	}

	public static SchemaBuilder buildNew() {
		return new SchemaBuilder();
	}
	
	public String fieldNameOf(int fieldNumber) {
		return fields[fieldNumber].name();
	}
	
	public int fieldsSize() {
		return fields.length;
	}
	
	public boolean hasSameSizeWith(Element[] elements) {
		return fields.length == elements.length;
	}
	
	public boolean hasDifferentSizeWith(Element[] elements) {
		return !hasSameSizeWith(elements);
	}
	
	public boolean isCompatibleWith(Element[] elements) {
		if (!hasSameSizeWith(elements)) {
			return false;
		}
		
		int dataLength = elements.length;
		for (int i = 0; i < dataLength; i++) {
			if (fields[i].isIncompatibleWith(elements[i])) {
				return false;
			}
		}

		return true;
	}

	public boolean isIncompatibleWith(Element[] data) {
		return !isCompatibleWith(data);
	}
	
	int fieldNumberOf(String fieldName) {
		int fieldLength = fields.length;
		for (int fieldNumber = 0; fieldNumber < fieldLength; fieldNumber++) {
			Field currentField = fields[fieldNumber];
			if (currentField.name().equals(fieldName)) {
				return fieldNumber;
			}
		}
		
		throw new IllegalArgumentException(fieldName + " is not exist.");
	}
	
	Field fieldOf(String fieldName) {
		return fields[fieldNumberOf(fieldName)];
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Schema.hashCode(fields);
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
		int fieldsLength = fields.length;
		for (int i = 0; i < fieldsLength; i++) {
			if (!fields[i].equals(other.fields[i])) {
				return false;
			}
		}
		return true;
	}

	public static class SchemaBuilder {
		private Vector proposedFields;

		private SchemaBuilder() {
			this.proposedFields = new Vector();
		}

		public SchemaBuilder withField(String fieldName, Datatype fieldType) {
			Field field = Field.of(fieldName, fieldType);
			if (proposedFields.contains(field)) {
				throw new IllegalArgumentException(
						"Specified field already exists");
			}
			proposedFields.addElement(field);
			return this;
		}

		public Schema thenGetResult() {
			Field[] fields = new Field[proposedFields.size()];
			proposedFields.copyInto(fields);

			return new Schema(fields);
		}
	}

}
