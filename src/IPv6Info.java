/**
 * Shawn Falconbury
 * March 3, 2024,
 * IPv6Info.java - This class extends the IPCalculator class and provides methods to calculate various network information for IPv6 addresses.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */

import java.net.InetAddress;
import java.math.BigInteger;
import java.net.UnknownHostException;

public class IPv6Info extends IPCalculator {

    public IPv6Info(String ipAddress, int cidrNotation) {
        super(ipAddress, cidrNotation); // Call the constructor of the superclass
    }

    @Override
    protected int calculateCIDRNotation() {
        return this.cidrNotation; // CIDR notation is already provided for IPv6 addresses.
    }

    @Override
    protected String calculateSubnetMask() {
        return ""; // Returning an empty string, as subnet masks are not typically used with IPv6.
    }

    @Override
    protected String calculateIPClass() {
        return ""; // Returning an empty string as IP classes are not applicable to IPv6.
    }

    // Calculate the total number of IPs -------------------------------------------
    public BigInteger calculateTotalIPs() {
        return BigInteger.valueOf(2).pow(128 - this.cidrNotation); // The total number of IPs is 2^(128 - CIDR notation)
    }

    // Calculate the network address ----------------------------------------------
    public String calculateNetwork() {
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte[] addressBytes = inetAddress.getAddress();
            // Apply CIDR mask to the IP address bytes to get the network address
            int maskFullBytes = this.cidrNotation / 8;
            int extraBits = this.cidrNotation % 8;
            for (int i = maskFullBytes; i < addressBytes.length; i++) {
                if (i == maskFullBytes) {
                    addressBytes[i] = (byte) (addressBytes[i] & 0xFF << (8 - extraBits));
                } else addressBytes[i] &= 0x00;
            }
            InetAddress networkAddress = InetAddress.getByAddress(addressBytes);
            return networkAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "Error calculating network";
        }
    }

    // Calculate the IP range -----------------------------------------------------
    public String calculateRange() {
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte[] addressBytes = inetAddress.getAddress();
            // Apply CIDR mask to the IP address bytes to get the network address
            int maskFullBytes = this.cidrNotation / 8;
            int extraBits = this.cidrNotation % 8;
            byte[] startAddressBytes = addressBytes.clone();
            byte[] endAddressBytes = addressBytes.clone();
            for (int i = maskFullBytes; i < addressBytes.length; i++) {
                if (i == maskFullBytes) {
                    startAddressBytes[i] = (byte) (startAddressBytes[i] & (0xFF << (8 - extraBits)));
                    endAddressBytes[i] = (byte) (endAddressBytes[i] | (0xFF >>> extraBits));
                } else {
                    endAddressBytes[i] = (byte) (endAddressBytes[i] | 0xFF);
                }
            }
            InetAddress startAddress = InetAddress.getByAddress(startAddressBytes);
            InetAddress endAddress = InetAddress.getByAddress(endAddressBytes);
            return startAddress.getHostAddress() + " - " + endAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "Error calculating range";
        }
    }

    // Calculate the full IP address ------------------------------------------------
    public String calculateFullIPAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "Error calculating full IP address";
        }

    }
}