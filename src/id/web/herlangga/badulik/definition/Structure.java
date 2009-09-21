package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private static final int ID_FIELD_NUMBER = 0;
	private static final DataType ID_FIELD_TYPE = DataType.LONG;

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

	public void addField(int fieldNumber, DataType fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int getObjectIDFieldNumber() {
		return ID_FIELD_NUMBER;
	}

	public final DataType getObjectIDDataType() {
		return ID_FIELD_TYPE;
	}

	public final DataType getDataTypeOfFieldNumber(int number) {
		Field field = (Field) fields.elementAt(number);
		return field.getFieldType();
	}

	public int fieldsSize() {
		return fields.size();
	}
}
