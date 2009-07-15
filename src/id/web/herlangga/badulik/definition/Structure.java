package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private Vector fields;

	public Structure() {
		this.fields = new Vector(5);
	}

	public void add(int fieldNumber, DataType fieldType) {
		Field field = new Field(fieldNumber, fieldType);
		add(field);
	}

	private void add(Field newField) {
		if (!fields.contains(newField)) {
			fields.addElement(newField);
		} else {
			throw new IllegalArgumentException("Field is already exists.");
		}

	}

	public Field getFieldNumber(int number) {
		return (Field) fields.elementAt(number);
	}

	public DataType getDataTypeOfFieldNumber(int number) {
		Field a = getFieldNumber(number);
		return a.getFieldType();
	}

	public Field[] toArray() {
		Field[] arrayOfFields = new Field[fieldsSize()];
		fields.copyInto(arrayOfFields);

		return arrayOfFields;
	}

	public int fieldsSize() {
		return fields.size();
	}
}
