/**
 * Wesley Shawn Falconbury
 * CSIS505 B01 - March 3, 2024,
 * IPValidator.java - This class provides methods to validate IPv4 and IPv6 addresses and subnet mask values.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IPValidator {

        // Validates IPv4 address format
        public static boolean validateIPv4Address(String ip) {
            if (ip == null || ip.isEmpty()) {
                return false;
            }
            String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
            return ip.matches(ipv4Pattern);
        }



    // Validates IPv6 address format
    public static boolean validateIPv6Address(String ip) { // Validate the IP address
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String pattern = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$"; // Regular expression pattern for IPv6 address
        return ip.matches(pattern);

    }

    // Validates subnet mask values for IPv4
    public static boolean validateMaskValues(int mask) {
        return mask >= 1 && mask <= 32; // Subnet mask values should be between 1 and 32 for IPv4 addresses
    }
}
