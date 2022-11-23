package com.diy.software.gui;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.payment.CreditPayment;
import com.diy.software.scansoft.AddItemScanned;
import com.jimmyselectronics.Item;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;
import javax.swing.JTabbedPane;

public class CustomJPanel extends JPanel {
	/**
	 * Creation of the panel
	 * 
	 */
	public CustomJPanel(Customer customer, DoItYourselfStationAR doItYourselfStation, CardIssuer bank, TouchScreen screen, Cart cart, JTabbedPane tabbedPane) {

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
		WeightLabel.setBounds(139, 382, 164, 34);
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
		potentialScanComboBox.setBounds(109, 349, 91, 23);
		add(potentialScanComboBox);
		
		JButton scanButton = new JButton("Scan Item");
		scanButton.setVerticalAlignment(SwingConstants.TOP);
		scanButton.setToolTipText("Use drop down to select");
		scanButton.setFont(new Font("Georgia", Font.PLAIN, 12));
		// Action event when "Scan" button clicked
		
		
		scanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				// Handling item to product verification process here as it wasn't handled anywhere else
				if(cart.addItemScanned.add(cBarcodedItems.get(potentialScanComboBox.getSelectedIndex()))) {
					priceTotal.setText("Cart Total: $" + (cart.addItemScanned.getTotal()));
					scannedItemPane.setText(scannedItemPane.getText() + "\n" + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(cBarcodedItems.get(potentialScanComboBox.getSelectedIndex()).getBarcode()).getDescription());
					WeightLabel.setText("Weight: " + cart.addItemScanned.getExpectedWeight() + " lbs");
					cBarcodedItems.remove(potentialScanComboBox.getSelectedIndex());
					potentialScanComboBox.removeItemAt(potentialScanComboBox.getSelectedIndex());
				}else{
					JOptionPane.showMessageDialog(getParent(), "Invalid Scan!", "Scan Error", JOptionPane.ERROR_MESSAGE);
				};
			}catch (Exception e1) {
				// Just silently ignore exception as null item
			}
				}
		});
		scanButton.setBounds(237, 349, 91, 23);
		add(scanButton);
		
		JLabel lblItemsToScan = new JLabel("Items to Scan");
		lblItemsToScan.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemsToScan.setForeground(Color.DARK_GRAY);
		lblItemsToScan.setFont(new Font("Georgia", Font.PLAIN, 13));
		lblItemsToScan.setBounds(103, 336, 97, 11);
		add(lblItemsToScan);
		
		JButton baggingButton = new JButton("Proceed to Bagging");
		baggingButton.setFont(new Font("Georgia", Font.PLAIN, 13));
		baggingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(5);
			}
		});
		baggingButton.setBounds(223, 440, 155, 23);
		add(baggingButton);
		
	}
}