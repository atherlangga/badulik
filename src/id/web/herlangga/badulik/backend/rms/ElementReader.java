package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class ElementReader {
	private static Hashtable mapping;
	
	static Element readFrom(byte[] rawData) throws IOException {
		ByteArrayInputStream source = new ByteArrayInputStream(rawData);
		DataInputStream input = new DataInputStream(source);
		
		Element result = readFrom(input);
		input.close();
		source.close();
		
		return result;
	}
	
	static Element readFrom(DataInput input) throws IOException {
		if (mapping == null) {
			mapping = createMapping();
		}
		Datatype type = Datatype.of(input.readByte());
		return ((ElementReader) mapping.get(type)).read(input);
	}
	
	abstract Element read(DataInput input) throws IOException;

	private static class IntTypeReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readInt());
		}
	}

	private static class LongTypeReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readLong());
		}
	}

	private static class StringTypeReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readUTF());
		}
	}

	private static class DateTypeReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(new Date(input.readLong()));
		}
	}

	private static class BoolTypeReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readBoolean());
		}
	}

	private static Hashtable createMapping() {
		Hashtable mapping = new Hashtable(5);
		mapping.put(Datatype.INT, new IntTypeReader());
		mapping.put(Datatype.LONG, new LongTypeReader());
		mapping.put(Datatype.STRING, new StringTypeReader());
		mapping.put(Datatype.DATE, new DateTypeReader());
		mapping.put(Datatype.BOOL, new BoolTypeReader());

		return mapping;
	}
}
