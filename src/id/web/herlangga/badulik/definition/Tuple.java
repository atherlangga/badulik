package id.web.herlangga.badulik.definition;

import java.util.*;

public class Tuple {
	private final Schema schema;
	private final Element[] elements;

	private Tuple(Schema schema, Element[] elements) {
		this.schema = schema;
		this.elements = elements;
	}

	public static TupleBuilder buildNewWithSchema(Schema schema) {
		return new TupleBuilder(schema);
	}

	public Schema schema() {
		return schema;
	}

	public Element elementOf(String fieldName) {
		return elements[schema.attributeNumberOf(fieldName)];
	}

	public Enumeration elementsEnumeration() {
		return new Enumeration() {
			private int currentCount = 0;
			private final int totalElements = elements.length;

			public Object nextElement() {
				Element current = elements[currentCount];
				currentCount++;
				return current;
			}

			public boolean hasMoreElements() {
				return currentCount < totalElements;
			}
		};
	}

	public static class TupleBuilder {
		private Schema schema;
		private Vector elementsCache;

		private TupleBuilder(Schema schema) {
			this.schema = schema;
			this.elementsCache = new Vector();
		}

		public TupleBuilder addField(String fieldName, Element element) {
			if (!schema.attributeOf(fieldName).isCompatibleWith(element)) {
				throw new IllegalArgumentException("Element " + fieldName
						+ " is incompatible with schema field.");
			}

			elementsCache.addElement(element);
			return this;
		}

		public Tuple getResult() {
			Element[] elements = new Element[elementsCache.size()];
			elementsCache.copyInto(elements);

			if (schema.isIncompatibleWith(elements)) {
				throw new IllegalStateException(
						"Supplied elements is incompatible with schema.");
			}

			return new Tuple(schema, elements);
		}
	}
}
