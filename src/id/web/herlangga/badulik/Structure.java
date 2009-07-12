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
			throw new IllegalArgumentException("Attribute is already exists.");
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
			if (a.getName().equals(name)) {
				return count;
			}
			
			count++;
		}
		
		return -1;
	}
	
	public int fieldSize() {
		return attributes.size();
	}
}
