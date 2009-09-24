package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private final int objectIdFieldNumber;
	private final Type objectIdFieldType;

	private final Vector fields = new Vector();

	private Structure(int objectIdFieldNumber, Type objectIdFieldType) {
		this.objectIdFieldNumber = objectIdFieldNumber;
		this.objectIdFieldType = objectIdFieldType;
	}

	public static StructureBuilder buildNew() {
		return new StructureBuilder();
	}

	public void addField(int fieldNumber, Type fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int objectIdFieldNumber() {
		return objectIdFieldNumber;
	}

	public Type objectIdFieldType() {
		return objectIdFieldType;
	}

	public Type typeOfFieldNumber(int number) {
		Field field = (Field) fields.elementAt(number);
		return field.fieldType();
	}

	public int fieldsSize() {
		return fields.size();
	}

	public boolean hasSameTypesWith(Datum[] data) {
		int fieldLength = data.length;
		for (int fieldNumber = 0; fieldNumber < fieldLength; fieldNumber++) {
			if (!typeOfFieldNumber(fieldNumber)
					.equals(data[fieldNumber].type())) {
				return false;
			}
		}

		return true;
	}

	public static class StructureBuilder {
		private Structure structure;

		public StructureBuilder withIdField(int idFieldNumber, Type idFieldType) {
			structure = new Structure(idFieldNumber, idFieldType);
			return this;
		}

		public StructureBuilder thenAddField(int fieldNumber, Type fieldType) {
			structure.addField(fieldNumber, fieldType);
			return this;
		}

		public Structure thenDoBuild() {
			return structure;
		}
	}

}
