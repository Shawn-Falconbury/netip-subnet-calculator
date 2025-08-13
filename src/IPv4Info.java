/**
 * Wesley Shawn Falconbury
 * CSIS505 B01 - March 3, 2024,
 * IPv4Info.java - This class extends the IPCalculator class and provides methods to calculate various network information for IPv4 addresses.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPv4Info extends IPCalculator {

    public IPv4Info(String ipAddress, String subnetMask) {
        super(ipAddress, subnetMask);
    }

    @Override
    protected int calculateCIDRNotation() {
        return subnetMaskToPrefixLength(this.subnetMask); } // Convert the subnet mask to CIDR notation
    @Override
    protected String calculateSubnetMask() {
        return cidrToSubnetMask(this.cidrNotation);
    } // Calculate the subnet mask

    // Calculate the IP class -----------------------------------------------------
    @Override
    protected String calculateIPClass() {
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte firstByte = inetAddress.getAddress()[0];
            if ((firstByte & 0xFF) <= 127) {
                return "A";
            } else if ((firstByte & 0xFF) >= 128 && (firstByte & 0xFF) <= 191) {
                return "B";
            } else if ((firstByte & 0xFF) >= 192 && (firstByte & 0xFF) <= 223) {
                return "C";
            } else if ((firstByte & 0xFF) >= 224 && (firstByte & 0xFF) <= 239) {
                return "D";
            } else if ((firstByte & 0xFF) >= 240) {
                return "E";
            }
        } catch (UnknownHostException e) {
        //    e.printStackTrace();
        }
        return "Unknown";
    }

    // Calculate the total number of hosts ---------------------------------------
    public long calculateTotalHosts() {
        return (long) Math.pow(2, (32 - this.cidrNotation)) - 2;
    }

    // Calculate the broadcast address -------------------------------------------
    public String calculateBroadcastAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte[] ipBytes = inetAddress.getAddress();
            int mask = ~(0xFFFFFFFF >>> this.cidrNotation);
            byte[] maskBytes = {
                    (byte) (mask >>> 24),
                    (byte) (mask >> 16 & 0xFF),
                    (byte) (mask >> 8 & 0xFF),
                    (byte) (mask & 0xFF)
            };

            for (int i = 0; i < ipBytes.length; i++) {
                ipBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
                maskBytes[i] = (byte) ~maskBytes[i];
                ipBytes[i] = (byte) (ipBytes[i] | maskBytes[i]);
            }

            return InetAddress.getByAddress(ipBytes).getHostAddress();
        } catch (UnknownHostException e) {
        //    e.printStackTrace();
            return "Error calculating broadcast address";
        }
    }

    // Calculate the usable host IP range --------------------------------------
    public String calculateUsableHostIPRange() {
        if (this.cidrNotation == 32) {
            return this.ipAddress; // Only one address in the subnet
        }
        try {
            // Calculate network address
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte[] ipBytes = inetAddress.getAddress();
            int mask = ~(0xFFFFFFFF >>> this.cidrNotation);
            byte[] maskBytes = {
                    (byte) (mask >>> 24),
                    (byte) (mask >> 16 & 0xFF),
                    (byte) (mask >> 8 & 0xFF),
                    (byte) (mask & 0xFF)
            };

            for (int i = 0; i < ipBytes.length; i++) {
                ipBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
            }

            // Start address is network address plus one
            ipBytes[3] = (byte) ((ipBytes[3] & 0xFF) + 1);
            String startAddress = InetAddress.getByAddress(ipBytes).getHostAddress();

            // Calculate broadcast address
            for (int i = 0; i < ipBytes.length; i++) {
                ipBytes[i] = (byte) (ipBytes[i] | ~maskBytes[i]);
            }

            // End address is broadcast address minus one
            ipBytes[3] = (byte) ((ipBytes[3] & 0xFF) - 1);
            String endAddress = InetAddress.getByAddress(ipBytes).getHostAddress();

            return startAddress + " - " + endAddress;
        } catch (UnknownHostException e) {
        //    e.printStackTrace();
            return "Error calculating usable host IP range";
        }
    }

    // Calculate the network address -------------------------------------------
    public String calculateNetworkAddress() {
        if (this.cidrNotation == 32) {
            return this.ipAddress; // Only one address in the subnet
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(this.ipAddress);
            byte[] ipBytes = inetAddress.getAddress();
            int mask = ~(0xFFFFFFFF >>> this.cidrNotation);
            byte[] maskBytes = {
                    (byte) (mask >>> 24),
                    (byte) (mask >> 16 & 0xFF),
                    (byte) (mask >> 8 & 0xFF),
                    (byte) (mask & 0xFF)
            };

            for (int i = 0; i < ipBytes.length; i++) {
                ipBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
            }

            return InetAddress.getByAddress(ipBytes).getHostAddress();
        } catch (UnknownHostException e) {
            //    e.printStackTrace();
            return "Error calculating network address";
        }
    }

    // Calculate the wildcard mask -------------------------------------------
    public String calculateWildcardMask() {
        if (this.cidrNotation == 32) {
            return "0.0.0.0"; // No wildcard mask for a 32-bit mask
        }
        try {
            int mask = ~(0xFFFFFFFF >>> this.cidrNotation);
            byte[] maskBytes = {
                    (byte) ~(mask >>> 24),
                    (byte) ~((mask >> 16) & 0xFF),
                    (byte) ~((mask >> 8) & 0xFF),
                    (byte) ~(mask & 0xFF)
            };
            return InetAddress.getByAddress(maskBytes).getHostAddress();
        } catch (UnknownHostException e) {
            //    e.printStackTrace();
            return "Error calculating wildcard mask";
        }
    }

    // Calculate the binary subnet mask -------------------------------------------
    public boolean isPrivate() {
        String[] parts = ipAddress.split("\\.");
        int firstOctet = Integer.parseInt(parts[0]);
        if (firstOctet == 10) { // Class A private range
            return true;
        } else // Class C private range
            if (firstOctet == 172 && Integer.parseInt(parts[1]) >= 16 && Integer.parseInt(parts[1]) <= 31) { // Class B private range
            return true;
        } else return firstOctet == 192 && Integer.parseInt(parts[1]) == 168;
    }

    // Convert subnet mask to prefix length -------------------------------------------
    public static int subnetMaskToPrefixLength(String subnetMask) {
        int prefixLength = 0;
        String[] parts = subnetMask.split("\\.");
        for (String part : parts) {
            int i = Integer.parseInt(part);
            while (i != 0) {
                prefixLength += i & 1;
                i >>>= 1;
            }
        }
        return prefixLength;
    }

        // Convert prefix length to subnet mask -------------------------------------------
        public String calculateBinarySubnetMask() {
            String[] octets = this.subnetMask.split("\\.");
            StringBuilder binarySubnetMask = new StringBuilder();
            for (String octet : octets) {
                int octetInt = Integer.parseInt(octet);
                String binaryOctet = Integer.toBinaryString(octetInt);
                binaryOctet = String.format("%8s", binaryOctet).replace(' ', '0'); // Pad with leading zeros
                binarySubnetMask.append(binaryOctet).append(".");
            }
            binarySubnetMask.deleteCharAt(binarySubnetMask.length() - 1); // Remove the trailing dot
            return binarySubnetMask.toString();
        }

    // Calculate the in-addr.arpa -------------------------------------------
    public String calculateInAddrArpa() {
        String[] octets = this.ipAddress.split("\\.");
        return octets[3] + "." + octets[2] + "." + octets[1] + "." + octets[0] + ".in-addr.arpa";
    }

    // Calculate the IPv4 Mapped Address -------------------------------------------
   // public String calculateIPv4MappedAddress() {
   //     return "0:0:0:0:0:ffff:" + ipAddress.replace(".", ":");
   // }

}
