package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class DatumReader {
	static Datum readFrom(byte[] rawData) throws IOException {
		ByteArrayInputStream source = new ByteArrayInputStream(rawData);
		DataInputStream input = new DataInputStream(source);
		
		Datum result = readFrom(input);
		input.close();
		source.close();
		
		return result;
	}
	
	static Datum readFrom(DataInput input) throws IOException {
		Type type = Type.of(input.readByte());
		return ((DatumReader) MAPPING.get(type)).read(input);
	}
	
	abstract Datum read(DataInput input) throws IOException;

	private static class IntTypeReader extends DatumReader {
		Datum read(DataInput input) throws IOException {
			return Datum.of(input.readInt());
		}
	}

	private static class LongTypeReader extends DatumReader {
		Datum read(DataInput input) throws IOException {
			return Datum.of(input.readLong());
		}
	}

	private static class StringTypeReader extends DatumReader {
		Datum read(DataInput input) throws IOException {
			return Datum.of(input.readUTF());
		}
	}

	private static class DateTypeReader extends DatumReader {
		Datum read(DataInput input) throws IOException {
			return Datum.of(new Date(input.readLong()));
		}
	}

	private static class BoolTypeReader extends DatumReader {
		Datum read(DataInput input) throws IOException {
			return Datum.of(input.readBoolean());
		}
	}

	private static Hashtable MAPPING = mapping();
	private static Hashtable mapping() {
		Hashtable mapping = new Hashtable();
		mapping.put(Type.INT, new IntTypeReader());
		mapping.put(Type.LONG, new LongTypeReader());
		mapping.put(Type.STRING, new StringTypeReader());
		mapping.put(Type.DATE, new DateTypeReader());
		mapping.put(Type.BOOL, new BoolTypeReader());

		return mapping;
	}
}
