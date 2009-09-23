package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private final Vector fields;
	private final int objectIdFieldNumber;
	private final Type objectIdFieldType;

	private Structure(int objectIdFieldNumber, Type objectIdFieldType) {
		this.objectIdFieldNumber = objectIdFieldNumber;
		this.objectIdFieldType = objectIdFieldType;
		fields = new Vector();
	}

	public static Structure createStructureWithIdField(int objectIdFieldNumber,
			Type objectIdFieldType) {
		Structure newStructure = new Structure(objectIdFieldNumber,
				objectIdFieldType);
		newStructure.addField(objectIdFieldNumber, objectIdFieldType);

		return newStructure;
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
}
