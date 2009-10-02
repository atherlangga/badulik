package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class DatumWriter {
	private static Hashtable mapping;
	abstract void writeTo(DataOutput destination, Datum datum)
			throws IOException;

	static DatumWriter for_(Type type) {
		if (mapping == null) {
			mapping = createMapping();
		}
		return (DatumWriter) mapping.get(type);
	}
	
	protected void writeTypeMarkerTo(DataOutput destination, Datum datum)
			throws IOException {
		byte typeMarker = datum.type().typeAsByte();
		destination.writeByte(typeMarker);
	}

	static class IntWriter extends DatumWriter {
		void writeTo(DataOutput destination, Datum datum) throws IOException {
			writeTypeMarkerTo(destination, datum);
			int value = ((Integer) datum.value()).intValue();
			destination.writeInt(value);
		}
	}

	static class LongWriter extends DatumWriter {
		void writeTo(DataOutput destination, Datum datum) throws IOException {
			writeTypeMarkerTo(destination, datum);
			long value = ((Long) datum.value()).longValue();
			destination.writeLong(value);
		}
	}

	static class StringWriter extends DatumWriter {
		void writeTo(DataOutput destination, Datum datum) throws IOException {
			writeTypeMarkerTo(destination, datum);
			String value = (String) datum.value();
			destination.writeUTF(value);
		}
	}

	static class DateWriter extends DatumWriter {
		void writeTo(DataOutput destination, Datum datum) throws IOException {
			writeTypeMarkerTo(destination, datum);
			long dateValue = ((Date) datum.value()).getTime();
			destination.writeLong(dateValue);
		}
	}

	static class BoolWriter extends DatumWriter {
		void writeTo(DataOutput destination, Datum datum) throws IOException {
			writeTypeMarkerTo(destination, datum);
			boolean value = ((Boolean) datum.value()).booleanValue();
			destination.writeBoolean(value);
		}
	}
	
	private static Hashtable createMapping() {
		Hashtable mapping = new Hashtable(5);
		mapping.put(Type.INT, new IntWriter());
		mapping.put(Type.LONG, new LongWriter());
		mapping.put(Type.STRING, new StringWriter());
		mapping.put(Type.DATE, new DateWriter());
		mapping.put(Type.BOOL, new BoolWriter());
		
		return mapping;
	}
}
