package id.web.herlangga.badulik;

public final class Backend {
	public static final Backend RMS = new Backend(1);
	
	private int type;
	
	private Backend(int type) {
		this.type = type;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Backend other = (Backend) obj;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
