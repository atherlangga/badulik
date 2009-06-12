package id.web.herlangga.badulik;

import java.util.Vector;

public class Structure {
	private Vector attributes;

	public Structure() {
		this.attributes = new Vector(5);
	}

	public void add(String name, Type type) {
		Attribute attribute = new Attribute(name, type);
		add(attribute);
	}

	public void add(Attribute attribute) {
		if (!attributes.contains(attribute)) {
			attributes.addElement(attribute);
		} else {
			try {
				throw new Exception("Name " + attribute.getName()
						+ " already exist.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Attribute getAttributeNumber(int number) {
		return (Attribute) attributes.elementAt(number);
	}

	public String getNameNumber(int number) {
		Attribute a = getAttributeNumber(number);
		return a.getName();
	}

	public Type getTypeNumber(int number) {
		Attribute a = getAttributeNumber(number);
		return a.getType();
	}

	public Type getTypeOf(String name) {
		int total = attributes.size();

		int count = 0;
		while (count < total) {
			Attribute a = (Attribute) attributes.elementAt(count);
			if (a.getName() == name) {
				return a.getType();
			}
			count++;
		}

		return null;
	}
	
	public int getNumberOf(String name) {
		int total = attributes.size();
		
		int count = 0;
		while (count < total) {
			Attribute a = (Attribute) attributes.elementAt(count);
			if (a.getName() == name) {
				return count;
			}
			
			count++;
		}
		
		return -1;
	}
}
