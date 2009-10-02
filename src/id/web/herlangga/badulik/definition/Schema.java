package id.web.herlangga.badulik.definition;

import java.util.Vector;

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
	
	public int fieldsSize() {
		return fields.length;
	}
	
	public boolean hasSameSizeWith(Element[] elements) {
		return fields.length == elements.length;
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
