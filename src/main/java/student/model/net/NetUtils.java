package student.model.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import student.model.formatters.Formats;

/** Network helpers for hostname lookup and ipapi requests. */
public final class NetUtils {
    /** URL template for the ipapi.co API: {ip}/{format}/. */
    private static final String API_URL_FORMAT = "https://ipapi.co/%s/%s/";

    private NetUtils() {
        // Prevent instantiation
    }


    /**
     * Builds a default XML ipapi URL for an IP address.
     *
     * @param ip the IP address to query
     * @return the API URL string for XML format
     */
    public static String getApiUrl(String ip) {
        return getApiUrl(ip, Formats.XML);
    }

    /**
     * Builds an ipapi URL for an IP address and response format.
     *
     * @param ip     the IP address to query
     * @param format the desired response format
     * @return the API URL string
     */
    public static String getApiUrl(String ip, Formats format) {
        return String.format(API_URL_FORMAT, ip, format.toString().toLowerCase());
    }


    /**
     * Resolves a hostname to an IP address.
     *
     * @param hostname the domain name to look up
     * @return the IP address string
     * @throws UnknownHostException if the hostname cannot be resolved
     */
    public static String lookUpIp(String hostname) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(hostname);
        String ip = address.getHostAddress();
        return ip;
    }


    /**
     * Opens a URL and returns its body stream, or an empty stream on failure.
     *
     * @param urlStr the URL to fetch
     * @return the response body as an InputStream, or an empty stream on error
     */
    public static InputStream getUrlContents(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // con.setRequestProperty("Content-Type", "application/xml");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            int status = con.getResponseCode();
            if (status == 200) {
                return con.getInputStream();
            } else {
                System.err.println("Failed to connect to " + urlStr);
            }

        } catch (Exception ex) {
            System.err.println("Failed to connect to " + urlStr);
        }
        return InputStream.nullInputStream();
    }


    /**
     * Fetches ipapi details for an IP address as XML.
     *
     * @param ip the IP address to query
     * @return the response body as an InputStream
     */
    public static InputStream getIpDetails(String ip) {
        return getIpDetails(ip, Formats.XML);
    }


    /**
     * Fetches ipapi details for an IP address in the requested format.
     *
     * @param ip     the IP address to query
     * @param format the desired response format
     * @return the response body as an InputStream
     */
    public static InputStream getIpDetails(String ip, Formats format) {
        String urlStr = getApiUrl(ip, format);
        return getUrlContents(urlStr);
    }

}
