package id.web.herlangga.badulik.backend.rms;

import id.web.herlangga.badulik.definition.*;

import java.io.*;
import java.util.*;

abstract class ElementWriter {
	private static final Hashtable MAPPING = createMapping();

	static ElementWriter of(Datatype type) {
		return (ElementWriter) MAPPING.get(type);
	}

	protected void writeTypeMarker(Element datum, DataOutput destination)
			throws IOException {
		byte typeMarker = datum.type().typeAsByte();
		destination.writeByte(typeMarker);
	}

	abstract void write(Element datum, DataOutput destination)
			throws IOException;

	static class IntWriter extends ElementWriter {
		void write(Element datum, DataOutput destination) throws IOException {
			writeTypeMarker(datum, destination);
			int value = ((Integer) datum.value()).intValue();
			destination.writeInt(value);
		}
	}

	static class LongWriter extends ElementWriter {
		void write(Element datum, DataOutput destination) throws IOException {
			writeTypeMarker(datum, destination);
			long value = ((Long) datum.value()).longValue();
			destination.writeLong(value);
		}
	}

	static class StringWriter extends ElementWriter {
		void write(Element datum, DataOutput destination) throws IOException {
			writeTypeMarker(datum, destination);
			String value = (String) datum.value();
			destination.writeUTF(value);
		}
	}

	static class DateWriter extends ElementWriter {
		void write(Element datum, DataOutput destination) throws IOException {
			writeTypeMarker(datum, destination);
			long dateValue = ((Date) datum.value()).getTime();
			destination.writeLong(dateValue);
		}
	}

	static class BoolWriter extends ElementWriter {
		void write(Element datum, DataOutput destination) throws IOException {
			writeTypeMarker(datum, destination);
			boolean value = ((Boolean) datum.value()).booleanValue();
			destination.writeBoolean(value);
		}
	}

	private static Hashtable createMapping() {
		Hashtable mapping = new Hashtable(5);
		mapping.put(Datatype.INT, new IntWriter());
		mapping.put(Datatype.LONG, new LongWriter());
		mapping.put(Datatype.STRING, new StringWriter());
		mapping.put(Datatype.DATE, new DateWriter());
		mapping.put(Datatype.BOOL, new BoolWriter());

		return mapping;
	}
}
