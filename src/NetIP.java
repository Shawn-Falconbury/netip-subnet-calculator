/**
 *Shawn Falconbury,
 * March 3, 2024,
 * NetIP.java - This program is a subnet calculator that allows the user to input an IPv4 or IPv6 address and subnet mask or prefix length to calculate various network information such as network address, broadcast address, IP class, and more.
 * -
 * Additional Sources that were used to assist in the development of this code:
 * Schildt, H., & Coward, D. (2024). Java: The complete reference, thirteenth edition (complete reference series) (13th ed.). McGraw Hill.
 * Ullenboom, C. (2022). Java: The comprehensive guide to java programming for professionals (First ed.). Rheinwerk Computing.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;

public class NetIP extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextField ipv4AddressField, ipv4SubnetMaskField, ipv6AddressField, ipv6PrefixLengthField;
    private JComboBox<Integer> maskBitsDropdown;
    private JTextArea outputArea;

    // Constructor
    public NetIP() {
        setTitle("NetIP - Subnet Calculator");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }
    // Initialize components
    private void initComponents() {
        tabbedPane = new JTabbedPane();

        JPanel ipv4Panel = new JPanel(new GridBagLayout());
        JPanel ipv6Panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

// IPv4 Components -----------------------------------------------------------
        ipv4AddressField = new JTextField(15);
        ipv4SubnetMaskField = new JTextField(15);
        Integer[] maskBits = {null, 24, 25, 26, 27, 28, 29, 30};
        maskBitsDropdown = new JComboBox<>(maskBits);
        maskBitsDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Optional");
                }
                return this;
            }
        });
        // Listener for the maskBitsDropdown
        maskBitsDropdown.addActionListener(e -> {
            Integer selectedItem = (Integer) maskBitsDropdown.getSelectedItem();
            if (selectedItem != null) {
                ipv4SubnetMaskField.setText(IPv4Info.cidrToSubnetMask(selectedItem));
            } else {
                ipv4SubnetMaskField.setText(""); // Clear the field if "Optional" is selected
            }
        });
        // IPv6 Components -----------------------------------------------------------
        ipv6AddressField = new JTextField(15);
        ipv6PrefixLengthField = new JTextField(15);

        // Buttons -------------------------------------------------------------------
        JButton calculateButtonIPv4 = new JButton("Calculate");
        JButton calculateButtonIPv6 = new JButton("Calculate");
        JButton copyButton = new JButton("Copy to Clipboard");
        JButton clearButton = new JButton("Clear");
        JButton closeButton = new JButton("Close");

        // Output Area (Text Area) ----------------------------------------------------
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        // Adding components to IPv4 Panel -------------------------------------------
        addComponent(ipv4Panel, new JLabel("IP Address:"), 0, 0, c);
        addComponent(ipv4Panel, ipv4AddressField, 1, 0, c);
        addComponent(ipv4Panel, calculateButtonIPv4, 2, 0, c);
        addComponent(ipv4Panel, new JLabel("Subnet Mask:"), 0, 1, c);
        addComponent(ipv4Panel, ipv4SubnetMaskField, 1, 1, c);
        addComponent(ipv4Panel, new JLabel("Mask Bits:"), 0, 2, c);
        addComponent(ipv4Panel, maskBitsDropdown, 1, 2, c);

        // Adding components to IPv6 Panel -------------------------------------------
        addComponent(ipv6Panel, new JLabel("IPv6 Address:"), 0, 0, c);
        addComponent(ipv6Panel, ipv6AddressField, 1, 0, c);
        addComponent(ipv6Panel, calculateButtonIPv6, 2, 0, c);
        addComponent(ipv6Panel, new JLabel("Prefix Length:"), 0, 1, c);
        addComponent(ipv6Panel, ipv6PrefixLengthField, 1, 1, c);

        // Adding Panels to TabbedPane ----------------------------------------------
        tabbedPane.addTab("IPv4", ipv4Panel);
        tabbedPane.addTab("IPv6", ipv6Panel);

        // Adding Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(copyButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        // Button Listeners ---------------------------------------------------------
        calculateButtonIPv4.addActionListener(new CalculateActionListener());
        calculateButtonIPv6.addActionListener(new CalculateActionListener());
        clearButton.addActionListener(e -> clearFields());
        closeButton.addActionListener(e -> System.exit(0));
        copyButton.addActionListener(e -> copyToClipboard());

        // Adding components to Frame -----------------------------------------------
        add(tabbedPane, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addComponent(JPanel panel, Component component, int x, int y, GridBagConstraints c) {
        c.gridx = x;
        c.gridy = y;
        panel.add(component, c);
    }

    private void clearFields() {
        ipv4AddressField.setText("");
        ipv4SubnetMaskField.setText("");
        ipv6AddressField.setText("");
        ipv6PrefixLengthField.setText("");
        outputArea.setText("");
    }
    // Copy the output to the system clipboard
    private void copyToClipboard() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(outputArea.getText()), null);
    }
    // Action Listener for the Calculate button
    private class CalculateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getSelectedIndex() == 0) { // IPv4
                // Perform IPv4 Calculation ------------------------------------------------
                String ip = ipv4AddressField.getText();
                String subnetMask = ipv4SubnetMaskField.getText();
                int prefixLength = IPv4Info.subnetMaskToPrefixLength(subnetMask); // Convert subnet mask to prefix length
                if (!IPValidator.validateIPv4Address(ip) || !IPValidator.validateMaskValues(prefixLength)) { // Validate IP and subnet mask
                    outputArea.setText("Invalid IPv4 Address or Subnet Mask.");
                    return;
                }
                IPv4Info ipv4Info = new IPv4Info(ip, subnetMask); // Create new IPv4Info object
                outputArea.setText(OutputFormatter.formatIPv4Output(ipv4Info)); // Format the output
            } else { // IPv6
                // Perform IPv6 Calculation ------------------------------------------------
                String ip = ipv6AddressField.getText();
                int prefixLength;
                try {
                    prefixLength = Integer.parseInt(ipv6PrefixLengthField.getText()); // Get prefix length
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid Prefix Length.");
                    return;
                }
                if (!IPValidator.validateIPv6Address(ip)) { // Validate IPv6 address
                    outputArea.setText("Invalid IPv6 Address.");
                    return;
                }
                IPv6Info ipv6Info = new IPv6Info(ip, prefixLength);
                outputArea.setText(OutputFormatter.formatIPv6Output(ipv6Info)); // Format the output
            }
        }
    }
    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetIP().setVisible(true));
    }
}