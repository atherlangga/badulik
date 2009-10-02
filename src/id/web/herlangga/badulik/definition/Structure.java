package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private final Field[] fields;

	private Structure(Field[] fields) {
		this.fields = fields;
	}

	public static StructureBuilder buildNew() {
		return new StructureBuilder();
	}

	public Type typeOfFieldNumber(int number) {
		Field field = (Field) fields[number];
		return field.fieldType();
	}

	public int fieldsSize() {
		return fields.length;
	}

	public boolean compatibleWith(Datum[] data) {
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

	public static class StructureBuilder {
		private Vector proposedFields;
		
		private StructureBuilder() {
			this.proposedFields = new Vector();
		}

		public StructureBuilder withField(String fieldName, Type fieldType) {
			Field field = new Field(fieldName, fieldType);
			proposedFields.addElement(field);
			return this;
		}

		public Structure thenGetResult() {
			Field[] fields = new Field[proposedFields.size()];
			proposedFields.copyInto(fields);
			
			return new Structure(fields);
		}
	}

}
