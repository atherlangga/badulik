package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private static final int ID_FIELD_NUMBER = 0;
	private static final Type ID_FIELD_TYPE = Type.LONG;

	private final Vector fields;

	private Structure() {
		fields = new Vector();
	}

	public static Structure createStructureWithIdAtFieldNumber(
			int objectIdFieldNumber) {
		Structure newStructure = new Structure();
		newStructure.addField(objectIdFieldNumber, ID_FIELD_TYPE);

		return newStructure;
	}

	public void addField(int fieldNumber, Type fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int objectIdFieldNumber() {
		return ID_FIELD_NUMBER;
	}

	public Type objectIdFieldType() {
		return ID_FIELD_TYPE;
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
}
