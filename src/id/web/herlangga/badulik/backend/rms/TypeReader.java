package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class TypeReader {
	public static TypeReader of(Type type) {
		return (TypeReader) typeReaderMapping().get(type);
	}

	public abstract Object readFrom(DataInput source)
			throws IOException;

	static class IntTypeReader extends TypeReader {
		public Object readFrom(DataInput source) throws IOException {
			return new Integer(source.readInt());
		}
	}
	
	static class LongTypeReader extends TypeReader {
		public Object readFrom(DataInput source) throws IOException {
			return new Long(source.readLong());
		}
	}
	
	static class StringTypeReader extends TypeReader {
		public Object readFrom(DataInput source) throws IOException {
			return source.readUTF();
		}
	}
	
	static class DateTypeReader extends TypeReader {
		public Object readFrom(DataInput source) throws IOException {
			return new Date(source.readLong());
		}
	}
	
	static class BoolTypeReader extends TypeReader {
		public Object readFrom(DataInput source) throws IOException {
			return new Boolean(source.readBoolean());
		}
	}
	
	private static Hashtable typeReaderMapping() {
		Hashtable typeReaderMapping = new Hashtable();
		typeReaderMapping.put(Type.INT, new IntTypeReader());
		typeReaderMapping.put(Type.LONG, new LongTypeReader());
		typeReaderMapping.put(Type.STRING, new StringTypeReader());
		typeReaderMapping.put(Type.DATE, new DateTypeReader());
		typeReaderMapping.put(Type.BOOL, new BoolTypeReader());
		
		return typeReaderMapping;
	}
}
