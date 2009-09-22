package id.web.herlangga.badulik.definition;

class Field {
	private final int fieldNumber;
	private final Type fieldType;
	
	Field(int fieldNumber, Type fieldType) {
		this.fieldNumber = fieldNumber;
		this.fieldType = fieldType;
	}

	final int fieldNumber() {
		return fieldNumber;
	}

	final Type fieldType() {
		return fieldType;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fieldNumber;
		result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
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
		if (fieldType == null) {
			if (other.fieldType != null)
				return false;
		} else if (!fieldType.equals(other.fieldType))
			return false;
		return true;
	}
	
}
