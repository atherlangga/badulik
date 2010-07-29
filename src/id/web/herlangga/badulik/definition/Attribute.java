package id.web.herlangga.badulik.definition;

class Attribute {
	private final String name;
	private final Datatype type;

	static Attribute of(String name, Datatype type) {
		return new Attribute(name, type);
	}

	private Attribute(String name, Datatype type) {
		this.name = name;
		this.type = type;
	}

	String name() {
		return name;
	}

	Datatype type() {
		return type;
	}

	boolean isCompatibleWith(Element element) {
		return type.equals(element.type());
	}

	boolean isIncompatibleWith(Element element) {
		return !isCompatibleWith(element);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
