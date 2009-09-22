package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private static final int ID_FIELD_NUMBER = 0;
	private static final Type ID_FIELD_TYPE = Type.LONG;

	private final Vector fields;

	private Structure() {
		fields = new Vector();
	}

	public static Structure createStructureWithIDAtFieldNumber(
			int objectIDFieldNumber) {
		Structure newStructure = new Structure();
		newStructure.addField(objectIDFieldNumber, ID_FIELD_TYPE);

		return newStructure;
	}

	public void addField(int fieldNumber, Type fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int getObjectIDFieldNumber() {
		return ID_FIELD_NUMBER;
	}

	public final Type getObjectIDDataType() {
		return ID_FIELD_TYPE;
	}

	public final Type getDataTypeOfFieldNumber(int number) {
		Field field = (Field) fields.elementAt(number);
		return field.getFieldType();
	}

	public int fieldsSize() {
		return fields.size();
	}
}
