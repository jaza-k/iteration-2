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
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;

public class CustomJPanel extends JPanel {
	/**
	 * Creation of the panel
	 */
	public CustomJPanel(Customer customer, DoItYourselfStationAR doItYourselfStation, CardIssuer bank) {
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
		WeightLabel.setBounds(148, 383, 164, 34);
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
		AddItemScanned addItemScanned = new AddItemScanned(ProductDatabases.BARCODED_PRODUCT_DATABASE, doItYourselfStation.scanner);
		scanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				// Handling item to product verification process here as it wasn't handled anywhere else
				if(addItemScanned.add(cBarcodedItems.get(potentialScanComboBox.getSelectedIndex()))) {
					priceTotal.setText("Cart Total: " + (addItemScanned.getTotal()));
					scannedItemPane.setText(scannedItemPane.getText() + "\n" + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(cBarcodedItems.get(potentialScanComboBox.getSelectedIndex()).getBarcode()).getDescription());
					WeightLabel.setText("Weight: " + addItemScanned.getExpectedWeight());
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
		
		JComboBox<String> cardComboBox = new JComboBox<String>();
		for (Card card : customer.wallet.cards) {
			cardComboBox.addItem(card.cardholder + ":" + card.kind);
		}
		//Add customer cards to the combo box here
		cardComboBox.setBounds(109, 446, 91, 23);
		add(cardComboBox);
		
		JLabel Wallet = new JLabel("Cards in Wallet");
		Wallet.setForeground(Color.DARK_GRAY);
		Wallet.setFont(new Font("Georgia", Font.PLAIN, 13));
		Wallet.setHorizontalAlignment(SwingConstants.CENTER);
		Wallet.setBounds(109, 434, 91, 11);
		add(Wallet);
		
		JLabel lblItemsToScan = new JLabel("Items to Scan");
		lblItemsToScan.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemsToScan.setForeground(Color.DARK_GRAY);
		lblItemsToScan.setFont(new Font("Georgia", Font.PLAIN, 13));
		lblItemsToScan.setBounds(103, 336, 97, 11);
		add(lblItemsToScan);
		
		JLabel pinLabel = new JLabel("Pin:");
		pinLabel.setBounds(247, 470, 60, 23);
		add(pinLabel);
		JTextField pinField = new JTextField();
		pinField.setBounds(277, 470, 40, 23);
		add(pinField);
		JButton PayButton = new JButton("Payment");
		PayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Action when "pay" button clicked
				try {
					//System.out.println(Integer.toString(cardComboBox.getSelectedIndex()));
					//System.out.println(pinField.getText().intern());
					//System.out.println(customer.wallet.cards.get(cardComboBox.getSelectedIndex()).cardholder);
					
					CreditPayment newpay = new CreditPayment();
					newpay.setCard(customer.wallet.cards.get(cardComboBox.getSelectedIndex()));
					newpay.setReader(doItYourselfStation.cardReader);
					newpay.setCardIssuer(bank);
					//System.out.println("Attempting to insert card.");
					newpay.insertCard(pinField.getText().intern()); //The intern() function will make sure the string is properly formatted.
					//System.out.println("Card successfully inserted");
					boolean flag = newpay.payForTotal(addItemScanned.getTotal());
					if (flag)
					{
						//doItYourselfStation.touchScreen.setVisible(false);
					}
					//else System.out.println("Transaction failed");
					
				}
				catch (InvalidPINException e2)
				{
					JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Payment Error", JOptionPane.ERROR_MESSAGE);
					//System.out.println(e2.toString());
				}
				catch (IOException e1) {
					// When Transcation Fails
					JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Payment Error", JOptionPane.ERROR_MESSAGE);
					//System.out.println(e1.toString());
				}
			}
		});
		PayButton.setFont(new Font("Georgia", Font.PLAIN, 13));
		PayButton.setBounds(237, 445, 91, 23);
		add(PayButton);
		
	
	}
}