package id.web.herlangga.badulik.definition;

public class StructureAndDataPair {
	private final Structure structure;
	private final Datum[] data;

	public static final StructureAndDataPair of(Structure structure,
			Datum[] data) {
		if (structure.fieldsSize() != data.length) {
			throw new IllegalArgumentException("Structure field size and values length must be equals");
		}
		return new StructureAndDataPair(structure, data);
	}

	private StructureAndDataPair(Structure structure, Datum[] data) {
		this.structure = structure;
		this.data = data;
	}
	
	public Structure structure() {
		return structure;
	}
	
	public Datum[] data() {
		return data;
	}
}
