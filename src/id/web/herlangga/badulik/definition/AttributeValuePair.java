package id.web.herlangga.badulik.definition;

public class AttributeValuePair {
	private Attribute attribute;
	private Object value;
	
	public AttributeValuePair(Attribute attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}
	
	public final Attribute getAttribute() {
		return attribute;
	}
	
	public final Object getValue() {
		return value;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
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
		AttributeValuePair other = (AttributeValuePair) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
