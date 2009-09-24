package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private final int idFieldNumber;
	private final Type idFieldType;

	private final Vector fields = new Vector();

	private Structure(int idFieldNumber, Type idFieldType) {
		this.idFieldNumber = idFieldNumber;
		this.idFieldType = idFieldType;
	}

	public static StructureBuilder buildNew() {
		return new StructureBuilder();
	}

	public void addField(int fieldNumber, Type fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int idFieldNumber() {
		return idFieldNumber;
	}

	public Type idFieldType() {
		return idFieldType;
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
			structure.addField(idFieldNumber, idFieldType);
			return this;
		}

		public StructureBuilder andField(int fieldNumber, Type fieldType) {
			structure.addField(fieldNumber, fieldType);
			return this;
		}

		public Structure thenGetResult() {
			return structure;
		}
	}

}
