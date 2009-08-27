package id.web.herlangga.badulik.definition;

class Field {
	private final int fieldNumber;
	private final DataType type;
	
	Field(int fieldNumber, DataType fieldType) {
		this.fieldNumber = fieldNumber;
		this.type = fieldType;
	}

	final int getFieldNumber() {
		return fieldNumber;
	}

	final DataType getFieldType() {
		return type;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fieldNumber;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (fieldNumber != other.fieldNumber)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
