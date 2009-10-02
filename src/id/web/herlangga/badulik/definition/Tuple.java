package id.web.herlangga.badulik.definition;

import java.util.*;

public class Tuple {
	private final Schema schema;
	private final Element[] elements;

	private Tuple(Schema schema, Element[] elements) {
		this.schema = schema;
		this.elements = elements;
	}
	
	public static TupleBuilder buildNew() {
		return new TupleBuilder();
	}
	
	public Schema schema() {
		return schema;
	}
	
	public Element elementOf(String fieldName) {
		return elements[schema.fieldNumberOf(fieldName)];
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
		private Vector elementsHolder;

		private TupleBuilder() {
			this.elementsHolder = new Vector();
		}

		public TupleBuilder withSchema(Schema schema) {
			this.schema = schema;
			return this;
		}

		public TupleBuilder thenAddColumn(String fieldName, Element element) {
			if (!schema.fieldOf(fieldName).isCompatibleWith(element)) {
				throw new IllegalArgumentException(
						"Element is not compatible with schema field.");
			}

			elementsHolder.addElement(element);
			return this;
		}
		
		public Tuple thenGetResult() {
			Element[] elements = new Element[elementsHolder.size()];
			elementsHolder.copyInto(elements);

			if (schema.hasDifferentSizeWith(elements)) {
				throw new IllegalStateException(
						"Supplied elements have different size " +
						"from schema size.");
			}
			if (schema.isIncompatibleWith(elements)) {
				throw new IllegalStateException(
						"Supplied elements is incompatible with schema.");
			}
			
			return new Tuple(schema, elements);
		}
	}
}
