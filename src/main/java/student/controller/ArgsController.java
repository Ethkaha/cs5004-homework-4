package student.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import student.model.DomainNameModel;
import student.model.DomainNameModel.DNRecord;
import student.model.formatters.Formats;

/**
 * A controller to handle command-line arguments.
 */
public class ArgsController {
    /** The domain name model used to look up and store records. */
    private DomainNameModel model;
    /** Path to the XML database file. */
    private String database = DomainNameModel.DATABASE;
    /** Output format for records. */
    private Formats format = Formats.PRETTY;
    /** Output stream to write results to. */
    private OutputStream output = System.out;
    /** Hostname or 'all' to display. */
    private String hostname = "all";
    /** Whether the help flag was passed. */
    private boolean showHelp = false;

    /** Creates a controller with default options. */
    public ArgsController() {
        this.model = DomainNameModel.getInstance(database);
    }

    /**
     * Returns help text shown to users.
     *
     * @return formatted help string describing usage and flags
     */
    public String getHelp() {
        return """
                DNInfoApp [hostname|all] [-f json|xml|csv|pretty] [-o file path] [-h | --help] [--data filepath]

                Looks up the information for a given hostname (url) or displays information for
                all domains in the database. Can be output in json, xml, csv, or pretty format.
                If -o file is provided, the output will be written to the file instead of stdout.

                --data is mainly used in testing to provide a different data file, defaults to the hostrecords.xml file.
                """;
    }

    /**
     * Parses args and runs the requested command.
     *
     * @param args command-line arguments to process
     */
    public void run(String[] args) {
        parseArgs(args);

        if (showHelp) {
            System.out.println(getHelp());
            return;
        }

        model = DomainNameModel.getInstance(database);

        try {
            if ("all".equalsIgnoreCase(hostname)) {
                List<DNRecord> records = model.getRecords();
                DomainNameModel.writeRecords(records, format, output);
            } else {
                DNRecord record = model.getRecord(hostname);
                DomainNameModel.writeRecords(List.of(record), format, output);
            }
        } finally {
            if (output != System.out) {
                try {
                    output.close();
                } catch (Exception e) {
                    throw new IllegalStateException("Unable to close output stream", e);
                }
            }
        }
    }

    /**
     * Parses supported command-line flags and positional hostname/all input.
     *
     * @param args command-line arguments to parse
     */
    private void parseArgs(String[] args) {
        if (args == null) {
            return;
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ("-h".equals(arg) || "--help".equals(arg)) {
                showHelp = true;
                return;
            } else if ("-f".equals(arg) && i + 1 < args.length) {
                Formats parsedFormat = Formats.containsValues(args[i + 1]);
                if (parsedFormat != null) {
                    format = parsedFormat;
                }
                i++;
            } else if ("-o".equals(arg) && i + 1 < args.length) {
                String outValue = args[i + 1];
                if (!"stdout".equalsIgnoreCase(outValue)) {
                    try {
                        output = new FileOutputStream(outValue);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Unable to open output file: " + outValue,
                                e);
                    }
                }
                i++;
            } else if ("--data".equals(arg) && i + 1 < args.length) {
                database = args[i + 1];
                i++;
            } else {
                // Keep the first hostname/all token and ignore any extras.
                if ("all".equals(hostname)) {
                    hostname = arg;
                }
            }
        }
    }
}
