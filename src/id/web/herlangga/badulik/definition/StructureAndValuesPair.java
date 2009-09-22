package id.web.herlangga.badulik.definition;

public class StructureAndValuesPair {
	private final Structure structure;
	private final Object[] values;

	public static final StructureAndValuesPair of(Structure structure,
			Object[] values) {
		if (structure.fieldsSize() != values.length) {
			throw new IllegalArgumentException("Structure field size and values length must be equals");
		}
		return new StructureAndValuesPair(structure, values);
	}

	private StructureAndValuesPair(Structure structure, Object[] values) {
		this.structure = structure;
		this.values = values;
	}
	
	public Structure structure() {
		return structure;
	}
	
	public Object[] values() {
		return values;
	}
}
