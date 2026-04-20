package student.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import student.model.formatters.DataFormatter;
import student.model.formatters.DomainXmlWrapper;
import student.model.formatters.Formats;
import student.model.net.NetUtils;

/**
 * Model for working with domain name records.
 */
public final class DomainNameModel {
    /** Default path to the XML database file. */
    public static final String DATABASE = "data/hostrecords.xml";
    /** Path to the active XML database file. */
    private final Path databasePath;
    /** In-memory list of loaded domain records. */
    private final List<DNRecord> records;

    /**
     * Creates a model backed by the provided XML database path.
     *
     * @param database path to the XML database file
     */
    private DomainNameModel(String database) {
        this.databasePath = Path.of(database);
        this.records = loadRecords(this.databasePath);
    }

    /**
     * Creates a model using the default database path.
     *
     * @return a new DomainNameModel backed by the default database
     */
    public static DomainNameModel getInstance() {
        return new DomainNameModel(DATABASE);
    }

    /**
     * Creates a model using a custom database path.
     *
     * @param database path to the XML database file
     * @return a new DomainNameModel backed by the given database
     */
    public static DomainNameModel getInstance(String database) {
        return new DomainNameModel(database);
    }

    /**
     * Returns a copy of all loaded records.
     *
     * @return a new list containing all current domain records
     */
    public List<DNRecord> getRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Returns a hostname record, fetching and persisting it if missing.
     *
     * @param hostname the domain name to look up
     * @return the matching DNRecord, fetched from the network if not cached
     */
    public DNRecord getRecord(String hostname) {
        if (hostname == null || hostname.isBlank()) {
            throw new IllegalArgumentException("hostname cannot be null or blank");
        }

        for (DNRecord record : records) {
            if (record.hostname().equalsIgnoreCase(hostname)) {
                return record;
            }
        }

        DNRecord newRecord = fetchAndBuildRecord(hostname);
        records.add(newRecord);
        saveRecords(records, databasePath);
        return newRecord;
    }

    /**
     * Writes records to the given stream in the requested format.
     *
     * @param records the list of domain records to write
     * @param format  the output format to use
     * @param out     the output stream to write to
     */
    public static void writeRecords(List<DNRecord> records, Formats format, OutputStream out) {
        DataFormatter.write(records, format, out);
    }

    /**
     * Looks up one hostname and maps the API result into a domain record.
     *
     * @param hostname the domain name to fetch details for
     * @return a new DNRecord populated from the ipapi response
     */
    private static DNRecord fetchAndBuildRecord(String hostname) {
        try {
            String ip = NetUtils.lookUpIp(hostname);
            ObjectMapper mapper = new ObjectMapper();

            try (InputStream in = NetUtils.getIpDetails(ip, Formats.JSON)) {
                IpApiResponse response = mapper.readValue(in, IpApiResponse.class);

                return new DNRecord(
                        hostname,
                        response.getIp(),
                        response.getCity(),
                        response.getRegion(),
                        response.getCountry(),
                        response.getPostal(),
                        response.getLatitude(),
                        response.getLongitude());
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unable to resolve hostname: " + hostname, e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch hostname details: " + hostname, e);
        }
    }

    /**
     * Loads records from disk, returning an empty list when no data exists.
     *
     * @param path the path to the XML database file
     * @return list of records loaded from the file, or an empty list
     */
    private static List<DNRecord> loadRecords(Path path) {
        try {
            if (!Files.exists(path) || Files.size(path) == 0) {
                return new ArrayList<>();
            }

            XmlMapper mapper = new XmlMapper();

            try (InputStream in = Files.newInputStream(path)) {
                DomainXmlWrapper wrapper = mapper.readValue(in, DomainXmlWrapper.class);

                if (wrapper.getDomain() == null) {
                    return new ArrayList<>();
                }

                return new ArrayList<>(wrapper.getDomain());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load records from " + path, e);
        }
    }

    /**
     * Persists all current records to the XML database file.
     *
     * @param records      the records to save
     * @param databasePath the path to write the XML file to
     */
    private static void saveRecords(List<DNRecord> records, Path databasePath) {
        try {
            Path parent = databasePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            XmlMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            DomainXmlWrapper wrapper = new DomainXmlWrapper(records);

            try (OutputStream out = Files.newOutputStream(
                    databasePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {
                mapper.writeValue(out, wrapper);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save records to " + databasePath, e);
        }
    }

    /** Minimal JSON view of fields used from ipapi. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class IpApiResponse {
        /** IP address returned by the API. */
        private String ip;
        /** City of the IP location. */
        private String city;
        /** Region of the IP location. */
        private String region;
        /** Country code of the IP location. */
        private String country;
        /** Postal code of the IP location. */
        private String postal;
        /** Latitude coordinate of the IP location. */
        private double latitude;
        /** Longitude coordinate of the IP location. */
        private double longitude;

        /**
         * Returns the IP address.
         *
         * @return ip address string
         */
        public String getIp() {
            return ip;
        }

        /**
         * Sets the IP address.
         *
         * @param ip the ip address
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * Returns the city.
         *
         * @return city string
         */
        public String getCity() {
            return city;
        }

        /**
         * Sets the city.
         *
         * @param city the city name
         */
        public void setCity(String city) {
            this.city = city;
        }

        /**
         * Returns the region.
         *
         * @return region string
         */
        public String getRegion() {
            return region;
        }

        /**
         * Sets the region.
         *
         * @param region the region name
         */
        public void setRegion(String region) {
            this.region = region;
        }

        /**
         * Returns the country.
         *
         * @return country code
         */
        public String getCountry() {
            return country;
        }

        /**
         * Sets the country.
         *
         * @param country the country code
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         * Returns the postal code.
         *
         * @return postal code string
         */
        public String getPostal() {
            return postal;
        }

        /**
         * Sets the postal code.
         *
         * @param postal the postal code
         */
        public void setPostal(String postal) {
            this.postal = postal;
        }

        /**
         * Returns the latitude.
         *
         * @return latitude value
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Sets the latitude.
         *
         * @param latitude the latitude value
         */
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        /**
         * Returns the longitude.
         *
         * @return longitude value
         */
        public double getLongitude() {
            return longitude;
        }

        /**
         * Sets the longitude.
         *
         * @param longitude the longitude value
         */
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    /**
     * Immutable model of one domain entry.
     *
     * @param hostname  the domain name
     * @param ip        the IP address
     * @param city      the city of the server location
     * @param region    the region of the server location
     * @param country   the country code of the server location
     * @param postal    the postal code of the server location
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JacksonXmlRootElement(localName = "domain")
    @JsonPropertyOrder({
            "hostname",
            "ip",
            "city",
            "region",
            "country",
            "postal",
            "latitude",
            "longitude"
    })
    public record DNRecord(
            String hostname,
            String ip,
            String city,
            String region,
            String country,
            String postal,
            double latitude,
            double longitude) {
    }
}
