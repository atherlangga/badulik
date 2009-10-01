package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class DatumReader {
	static DatumReader of(DataInput source) throws IOException {
		Type type = Type.of(source.readByte());
		return (DatumReader) valueReaderMapping().get(type);
	}

	abstract Datum readFrom(DataInput source) throws IOException;

	static class IntTypeReader extends DatumReader {
		Datum readFrom(DataInput source) throws IOException {
			return Datum.of(source.readInt());
		}
	}

	static class LongTypeReader extends DatumReader {
		Datum readFrom(DataInput source) throws IOException {
			return Datum.of(source.readLong());
		}
	}

	static class StringTypeReader extends DatumReader {
		Datum readFrom(DataInput source) throws IOException {
			return Datum.of(source.readUTF());
		}
	}

	static class DateTypeReader extends DatumReader {
		Datum readFrom(DataInput source) throws IOException {
			return Datum.of(new Date(source.readLong()));
		}
	}

	static class BoolTypeReader extends DatumReader {
		Datum readFrom(DataInput source) throws IOException {
			return Datum.of(source.readBoolean());
		}
	}

	private static Hashtable valueReaderMapping() {
		Hashtable valueReaderMapping = new Hashtable();
		valueReaderMapping.put(Type.INT, new IntTypeReader());
		valueReaderMapping.put(Type.LONG, new LongTypeReader());
		valueReaderMapping.put(Type.STRING, new StringTypeReader());
		valueReaderMapping.put(Type.DATE, new DateTypeReader());
		valueReaderMapping.put(Type.BOOL, new BoolTypeReader());

		return valueReaderMapping;
	}
}
