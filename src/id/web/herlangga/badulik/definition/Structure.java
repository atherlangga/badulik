package id.web.herlangga.badulik.definition;

import java.util.Vector;

public class Structure {
	private Vector fields;

	public Structure() {
		this.fields = new Vector(5);
	}

	public void add(int fieldNumber, DataType fieldType) {
		Field attribute = new Field(fieldNumber, fieldType);
		add(attribute);
	}

	private void add(Field attribute) {
		if (!fields.contains(attribute)) {
			fields.addElement(attribute);
		} else {
			throw new IllegalArgumentException("Field is already exists.");
		}

	}

	public Field getFieldNumber(int number) {
		return (Field) fields.elementAt(number);
	}

	public DataType getFieldTypeOfFieldNumber(int number) {
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
