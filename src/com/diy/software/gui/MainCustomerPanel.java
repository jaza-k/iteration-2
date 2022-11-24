package com.diy.software.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.SwingConstants;

import com.diy.hardware.BarcodedProduct;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.controllers.ScaleController;
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
        priceTotal.setBounds(175, 321, 144, 14);
        add(priceTotal);

        JLabel WeightLabel = new JLabel("Item Weight: ???");
        WeightLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        WeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        WeightLabel.setBounds(142, 393, 164, 23);
        add(WeightLabel);

        JTextPane scannedItemPane = new JTextPane();
        scannedItemPane.setEditable(false);
        scannedItemPane.setBounds(56, 11, 350, 304);
        add(scannedItemPane);
        

        JButton scanButton = new JButton("Scan Item");
        scanButton.setToolTipText("");
        scanButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        
        // Action event when "Scan" button clicked
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    scanItem();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(getParent(), "Invalid Item!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                }
            }
			private void scanItem() {
				//if(stationLogic.scaleController.getStatus() == ScaleController.Status.READY) {
				if(stationLogic.getStatus() == DoItYourselfStationLogic.Status.READY) {
	            	customer.selectNextItem();
	                customer.scanItem();
				}

                if(stationLogic.getStatus() != WAITING_FOR_WEIGHT) {
                    JOptionPane.showMessageDialog(getParent(), "Scan Failed!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

				// At this point, the status should be "Waiting for Weight"
				JOptionPane.showMessageDialog(getParent(), "Place Item in Bagging!", "Bagging Update", JOptionPane.INFORMATION_MESSAGE);
				bagItem();
			}

			private void bagItem() {
					// Customer "puts" the item into the bagging area so now we can compare expected vs actual weight
					customer.placeItemInBaggingArea();
					
					WeightLabel.setText("Weight: " + stationLogic.scaleController.getExpectedWeightInGrams() + " grams");
					priceTotal.setText("Cart Total: $" + (stationLogic.scannerController.getTotal()));
					
					StringBuilder stringBuilder = new StringBuilder();
					for (BarcodedProduct barcodedProduct : stationLogic.scannerController.getScannedItems()) {
						stringBuilder.append(barcodedProduct.getDescription() + "\t\t\t\t$" + barcodedProduct.getPrice() + "\n");
					}
					scannedItemPane.setText(stringBuilder.toString());
	                
					//if(stationLogic.scaleController.getStatus() == ScaleController.Status.DISCREPANCY) {
					if(stationLogic.getStatus() == DoItYourselfStationLogic.Status.DISCREPANCY) {
						tabbedPane.setSelectedIndex(6);
					}
                }
        });
        scanButton.setBounds(132, 346, 177, 36);
        add(scanButton);
  
        // Button for switching to payment tab
        JButton switchToPaymentButton = new JButton("Proceed To Bagging");
        switchToPaymentButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        // Action event when "Proceed" button clicked
        switchToPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(1);
            }
        });
        switchToPaymentButton.setBounds(155, 425, 141, 35);
        add(switchToPaymentButton);
    }
}