package student.model.formatters;

import java.io.PrintStream;
import java.io.OutputStream;
import java.util.Collection;
import javax.annotation.Nonnull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import student.model.DomainNameModel.DNRecord;

/**
 * A class to format the data in different ways.
 */
public final class DataFormatter {

    private DataFormatter() {

    }

    /**
     * Writes records in readable multi-line text format.
     *
     * @param records the records to print
     * @param out     the output stream to write to
     */
    private static void prettyPrint(Collection<DNRecord> records, OutputStream out) {
        PrintStream pout = new PrintStream(out); // so i can use println
        for (DNRecord record : records) {
            prettySingle(record, pout);
            pout.println();
        }
    }

    /**
     * Writes one record in the required pretty layout.
     *
     * @param record the record to print
     * @param out    the print stream to write to
     */
    private static void prettySingle(@Nonnull DNRecord record, @Nonnull PrintStream out) {
        out.println(record.hostname());
        out.println("             IP: " + record.ip());
        out.println("       Location: " + record.city() + ", " + record.region() + ", "
                + record.country() + ", " + record.postal());
        out.println("    Coordinates: " + record.latitude() + ", " + record.longitude());

    }

    /**
     * Writes records as XML.
     *
     * @param records the records to serialize
     * @param out     the output stream to write to
     */
    private static void writeXmlData(Collection<DNRecord> records, OutputStream out) {
        try {
            XmlMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            DomainXmlWrapper wrapper = new DomainXmlWrapper(records);

            mapper.writeValue(out, wrapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write XML data", e);
        }

    }

    /**
     * Writes records as JSON.
     *
     * @param records the records to serialize
     * @param out     the output stream to write to
     */
    private static void writeJsonData(Collection<DNRecord> records, OutputStream out) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            mapper.writeValue(out, records);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write JSON data", e);
        }
    }

    /**
     * Writes records as CSV.
     *
     * @param records the records to serialize
     * @param out     the output stream to write to
     */
    private static void writeCSVData(Collection<DNRecord> records, OutputStream out) {
        try {
            PrintStream pout = new PrintStream(out);

            pout.println("hostname,ip,city,region,country,postal,latitude,longitude");

            for (DNRecord record : records) {
                pout.println(
                        record.hostname() + ","
                                + record.ip() + ","
                                + record.city() + ","
                                + record.region() + ","
                                + record.country() + ","
                                + record.postal() + ","
                                + record.latitude() + ","
                                + record.longitude());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write CSV data", e);
        }
    }

    /**
     * Writes records in the requested output format.
     *
     * @param records the records to write
     * @param format  the output format to use
     * @param out     the output stream to write to
     */
    public static void write(@Nonnull Collection<DNRecord> records, @Nonnull Formats format,
            @Nonnull OutputStream out) {

        switch (format) {
            case XML:
                writeXmlData(records, out);
                break;
            case JSON:
                writeJsonData(records, out);
                break;
            case CSV:
                writeCSVData(records, out);
                break;
            default:
                prettyPrint(records, out);

        }
    }

}
