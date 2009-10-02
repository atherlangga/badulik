package id.web.herlangga.badulik.definition;

class Field {
	private final String fieldName;
	private final Datatype fieldType;
	
	Field(String fieldName, Datatype fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}

	String fieldName() {
		return fieldName;
	}

	Datatype fieldType() {
		return fieldType;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result
				+ ((fieldType == null) ? 0 : fieldType.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Field other = (Field) obj;
		if (fieldName == null) {
			if (other.fieldName != null) {
				return false;
			}
		} else if (!fieldName.equals(other.fieldName)) {
			return false;
		}
		if (fieldType == null) {
			if (other.fieldType != null) {
				return false;
			}
		} else if (!fieldType.equals(other.fieldType)) {
			return false;
		}
		return true;
	}
	
}
