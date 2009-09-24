package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class DatumWriter {
	abstract void writeTo(DataOutput destination, Datum datum)
			throws IOException;

	protected void writeTypeMarkerTo(DataOutput destination, Datum datum)
			throws IOException {
		byte typeMarker = datum.type().typeAsByte();
		destination.writeByte(typeMarker);
	}

	static DatumWriter forDatum(Datum datum) {
		return (DatumWriter) valueWriterMapping().get(datum.type());
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
	
	private static Hashtable valueWriterMapping() {
		Hashtable valueWriterMapping = new Hashtable();
		valueWriterMapping.put(Type.INT, new IntWriter());
		valueWriterMapping.put(Type.LONG, new LongWriter());
		valueWriterMapping.put(Type.STRING, new StringWriter());
		valueWriterMapping.put(Type.DATE, new DateWriter());
		valueWriterMapping.put(Type.BOOL, new BoolWriter());
		
		return valueWriterMapping;
	}
}
