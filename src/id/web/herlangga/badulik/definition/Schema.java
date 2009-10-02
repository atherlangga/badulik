package id.web.herlangga.badulik.definition;

import java.util.Vector;

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

	public boolean isCompatibleWith(Element[] data) {
		int dataLength = data.length;
		
		if (fields.length != dataLength) {
			return false;
		}
		for (int fieldNumber = 0; fieldNumber < dataLength; fieldNumber++) {
			if (!typeOfFieldNumber(fieldNumber)
					.equals(data[fieldNumber].type())) {
				return false;
			}
		}

		return true;
	}
	
	public boolean isNotCompatibleWith(Element[] data) {
		return !isCompatibleWith(data);
	}
	
	private Datatype typeOfFieldNumber(int number) {
		Field field = (Field) fields[number];
		return field.fieldType();
	}

	public static class SchemaBuilder {
		private Vector proposedFields;
		
		private SchemaBuilder() {
			this.proposedFields = new Vector();
		}

		public SchemaBuilder withField(String fieldName, Datatype fieldType) {
			Field field = new Field(fieldName, fieldType);
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
