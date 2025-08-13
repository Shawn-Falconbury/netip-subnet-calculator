/**
 * Wesley Shawn Falconbury,
 * March 3, 2024,
 * IPCalculator.java - This abstract class provides methods to calculate CIDR notation, subnet mask, and IP class for IPv4 and IPv6 addresses.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class IPCalculator {
    protected String ipAddress;
    protected String subnetMask;
    protected int cidrNotation;
    protected String ipClass;

    public IPCalculator(String ipAddress, String subnetMask) { // Constructor for the subnet mask
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.cidrNotation = calculateCIDRNotation();
        this.ipClass = calculateIPClass();
    }

    public IPCalculator(String ipAddress, int cidrNotation) { // Constructor for the CIDR notation
        this.ipAddress = ipAddress;
        this.cidrNotation = cidrNotation;
        this.subnetMask = calculateSubnetMask();
        this.ipClass = calculateIPClass();
    }

    protected abstract int calculateCIDRNotation(); // Abstract method to calculate CIDR notation

    protected abstract String calculateSubnetMask(); // Abstract method to calculate subnet mask

    protected abstract String calculateIPClass(); // Abstract method to calculate IP class

    public String getIpAddress() {
        return ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    // Utility method to convert CIDR notation to subnet mask
    protected static String cidrToSubnetMask(int cidr) { // Convert CIDR notation to subnet mask
        int mask = 0xffffffff << (32 - cidr); // Left shift 32 - CIDR notation
        byte[] bytes = new byte[]{ // Convert the mask to bytes
                (byte)(mask >>> 24), (byte)(mask >> 16 & 0xff), (byte)(mask >> 8 & 0xff), (byte)(mask & 0xff) // Right shift the mask and store the bytes
        };
        InetAddress inetAddress; // Create an InetAddress object
        try {
            inetAddress = InetAddress.getByAddress(bytes);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

}