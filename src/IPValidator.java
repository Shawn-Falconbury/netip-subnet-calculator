/**
 * Shawn Falconbury
 * March 3, 2024,
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
    public static boolean validateIPv4Address(String ip) { // Validate the IP address
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }
            InetAddress inet = InetAddress.getByName(ip);
            return inet.getHostAddress().equals(ip) && inet instanceof Inet4Address; // Check if the IP address is valid and is an IPv4 address
        } catch (UnknownHostException ex) {
            return false;
        }
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
        return mask >= 24 && mask <= 30; // Subnet mask values should be between 24 and 30 for IPv4 addresses
    }
}