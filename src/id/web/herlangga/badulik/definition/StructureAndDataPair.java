package id.web.herlangga.badulik.definition;

public class StructureAndDataPair {
	private final Structure structure;
	private final Datum[] data;

	public static final StructureAndDataPair of(Structure structure,
			Datum[] data) {
		if (!structure.hasSameTypesWith(data)) {
			throw new IllegalArgumentException(
					"Structure and data types are not equals");
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
