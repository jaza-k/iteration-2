package com.diy.software.gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.necchi.BarcodedItem;

import static com.diy.software.DoItYourselfStationLogic.Status.READY;
import static com.diy.software.DoItYourselfStationLogic.Status.WAITING_FOR_WEIGHT;

public class MainCustomerPanel extends JPanel {
    /**
     * Creation of the panel
     */
    public MainCustomerPanel(Customer customer, DoItYourselfStationLogic stationLogic, JTabbedPane tabbedPane) {


        setForeground(new Color(128, 128, 255));
        setBackground(SystemColor.inactiveCaption);
        setLayout(null);

        JLabel priceTotal = new JLabel("Current Total: $");
        priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
        priceTotal.setBounds(178, 321, 196, 14);
        add(priceTotal);

        JLabel WeightLabel = new JLabel("Item Weight: ???");
        WeightLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        WeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        WeightLabel.setBounds(145, 393, 164, 34);
        add(WeightLabel);

        JTextPane scannedItemPane = new JTextPane();
        scannedItemPane.setEditable(false);
        scannedItemPane.setBounds(45, 11, 329, 304);
        add(scannedItemPane);

        JComboBox<String> potentialScanComboBox = new JComboBox<String>();
        //Adds all the barcoded items into the potential scannable combo box
        ArrayList<BarcodedItem> cBarcodedItems = new ArrayList<BarcodedItem>();
        for (int i = 1; i <= customer.shoppingCart.size(); i++) {
            potentialScanComboBox.addItem("Item " + i);
            cBarcodedItems.add((BarcodedItem) customer.shoppingCart.get(i - 1));
        }
        potentialScanComboBox.setBounds(113, 359, 91, 23);
        add(potentialScanComboBox);

        JButton scanButton = new JButton("Scan Item");
        scanButton.setVerticalAlignment(SwingConstants.TOP);
        scanButton.setToolTipText("Use drop down to select");
        scanButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        // Action event when "Scan" button clicked
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Prevent scan if not ready
                    if(stationLogic.getStatus() != READY) {
                        JOptionPane.showMessageDialog(getParent(), "Not Ready To Scan!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Handling item to product verification process here as it wasn't handled anywhere else
                    customer.selectNextItem();
                    customer.scanItem();
                    // Stop if scan failed
                    if(stationLogic.getStatus() != WAITING_FOR_WEIGHT) {
                        JOptionPane.showMessageDialog(getParent(), "Scan Failed!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update the GUI
                    customer.placeItemInBaggingArea();
                    priceTotal.setText("Cart Total: " + (stationLogic.scannerController.getTotal()));
                    scannedItemPane.setText(
                            String.join("\n", stationLogic.scannerController.getScannedItems().stream().map(item -> item.getDescription()).toList())
                    );
                    WeightLabel.setText("Weight: " + stationLogic.scaleController.getExpectedWeightInGrams() + " lbs");
                    cBarcodedItems.remove(potentialScanComboBox.getSelectedIndex());
                    potentialScanComboBox.removeItemAt(potentialScanComboBox.getSelectedIndex());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(getParent(), "Invalid Item!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        scanButton.setBounds(250, 359, 91, 23);
        add(scanButton);


        JLabel lblItemsToScan = new JLabel("Items to Scan");
        lblItemsToScan.setHorizontalAlignment(SwingConstants.CENTER);
        lblItemsToScan.setForeground(Color.DARK_GRAY);
        lblItemsToScan.setFont(new Font("Georgia", Font.PLAIN, 13));
        lblItemsToScan.setBounds(107, 346, 97, 11);
        add(lblItemsToScan);


        // Button for switching to payment tab
        JButton switchToPaymentButton = new JButton("Proceed To Bagging");
        switchToPaymentButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        // Action event when "Proceed" button clicked
        switchToPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(5);
            }
        });
        switchToPaymentButton.setBounds(233, 434, 141, 35);
        add(switchToPaymentButton);
    }
}