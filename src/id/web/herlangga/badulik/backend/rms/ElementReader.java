package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class ElementReader {
	private static Hashtable mapping = createMapping();
	
	static Element readFrom(byte[] rawData) throws IOException {
		ByteArrayInputStream source = new ByteArrayInputStream(rawData);
		DataInputStream input = new DataInputStream(source);
		
		Element result = readFrom(input);
		input.close();
		source.close();
		
		return result;
	}
	
	static Element readFrom(DataInput input) throws IOException {
		Datatype type = Datatype.of(input.readByte());
		return ((ElementReader) mapping.get(type)).read(input);
	}
	
	abstract Element read(DataInput input) throws IOException;

	private static class IntReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readInt());
		}
	}

	private static class LongReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readLong());
		}
	}

	private static class StringReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readUTF());
		}
	}

	private static class DateReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(new Date(input.readLong()));
		}
	}

	private static class BoolReader extends ElementReader {
		Element read(DataInput input) throws IOException {
			return Element.of(input.readBoolean());
		}
	}

	private static Hashtable createMapping() {
		Hashtable mapping = new Hashtable(5);
		mapping.put(Datatype.INT, new IntReader());
		mapping.put(Datatype.LONG, new LongReader());
		mapping.put(Datatype.STRING, new StringReader());
		mapping.put(Datatype.DATE, new DateReader());
		mapping.put(Datatype.BOOL, new BoolReader());

		return mapping;
	}
}
