package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private static final int ID_FIELDNUMBER = 0;
	private static final DataType ID_FIELDTYPE = DataType.LONG;
	
	private final Vector fields = new Vector();

	private Structure() {
	}

	public static Structure createStructure(int domainObjectIDFieldNumber) {
		Structure newStructure = new Structure();
		Field domainObjectIDField = new Field(domainObjectIDFieldNumber,
				DataType.LONG);
		newStructure.fields.addElement(domainObjectIDField);

		return newStructure;
	}

	public void add(int fieldNumber, DataType fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int getDomainObjectIDFieldNumber() {
		return ID_FIELDNUMBER;
	}

	public final DataType getDomainObjectIDDataType() {
		return ID_FIELDTYPE;
	}

	public final DataType getDataTypeOfFieldNumber(int number) {
		Field field = (Field) fields.elementAt(number);
		return field.getFieldType();
	}

	public int fieldsSize() {
		return fields.size();
	}
}
