/**
 * Shawn Falconbury,
 * March 3, 2024,
 * OutputFormatter.java - This class provides methods to format the output of the network information for both IPv4 and IPv6 addresses.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */
import java.util.StringJoiner;

public class OutputFormatter {

    // Format the output for IPv4 addresses ------------------------------------------------
    public static String formatIPv4Output(IPv4Info ipv4Info) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("IP Address: " + ipv4Info.getIpAddress());
        sj.add("Network Address: " + ipv4Info.calculateNetworkAddress());
        sj.add("Usable Host IP Range: " + ipv4Info.calculateUsableHostIPRange());
        sj.add("Total Number of Hosts: " + ipv4Info.calculateTotalHosts());
        sj.add("Number of Usable Hosts: " + (ipv4Info.calculateTotalHosts() - 2));
        sj.add("Subnet Mask: " + ipv4Info.getSubnetMask());
        sj.add("Wildcard Mask: " + ipv4Info.calculateWildcardMask());
        sj.add("Binary Subnet Mask: " + ipv4Info.calculateBinarySubnetMask());
        sj.add("IP Class: " + ipv4Info.calculateIPClass());
        sj.add("CIDR Notation: /" + ipv4Info.calculateCIDRNotation());
        sj.add("IP Type: " + (ipv4Info.isPrivate() ? "Private" : "Public"));
        sj.add("Broadcast Address: " + ipv4Info.calculateBroadcastAddress());
        return sj.toString();
    }
    // Format the output for IPv6 addresses ------------------------------------------------
    public static String formatIPv6Output(IPv6Info ipv6Info) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("IP Address: " + ipv6Info.getIpAddress());
        sj.add("Full IP Address: " + ipv6Info.calculateFullIPAddress());
        sj.add("Total IP Addresses: " + ipv6Info.calculateTotalIPs());
        sj.add("Network: " + ipv6Info.calculateNetwork());
        sj.add("IP Range: " + ipv6Info.calculateRange());
        return sj.toString();
    }
}