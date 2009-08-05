package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private Vector fields;
	private Field domainObjectIDField;

	public Structure(int domainObjectIDFieldNumber) {
		this.fields = new Vector(5);
		this.domainObjectIDField = new Field(domainObjectIDFieldNumber,
				DataType.LONG);

		fields.addElement(domainObjectIDField);
	}

	public void add(int fieldNumber, DataType fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		fields.addElement(field);
	}

	public int getDomainObjectIDFieldNumber() {
		return domainObjectIDField.getFieldNumber();
	}
	
	public final DataType getDomainObjectIDDataType() {
		return domainObjectIDField.getFieldType();
	}

	public final DataType getDataTypeOfFieldNumber(int number) {
		Field field = (Field) fields.elementAt(number);
		return field.getFieldType();
	}

	public int fieldsSize() {
		return fields.size();
	}
}
