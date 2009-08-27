package id.web.herlangga.badulik.definition;

public class DataTypeAndValuePair {
	private final DataType dataType;
	private final Object value;
	
	public DataTypeAndValuePair(DataType dataType, Object value) {
		this.dataType = dataType;
		this.value = value;
	}
	
	public final DataType getDataType() {
		return dataType;
	}
	
	public final Object getValue() {
		return value;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataTypeAndValuePair other = (DataTypeAndValuePair) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
