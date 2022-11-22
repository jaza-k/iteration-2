package com.diy.software.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.payment.CreditPayment;
import com.diy.software.scansoft.AddItemScanned;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;

public class IterationTwoPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public IterationTwoPanel(Customer customer,DoItYourselfStationAR doItYourselfStationAR ,TouchScreen touchScreen, CardIssuer bank) {
		setForeground(new Color(128, 128, 255));
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JLabel priceTotal = new JLabel("Current Total: $");
		priceTotal.setHorizontalAlignment(SwingConstants.CENTER);
		priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
		priceTotal.setBounds(47, 376, 164, 34);
		add(priceTotal);
		
		JLabel WeightLabel = new JLabel("Item Weight: ???");
		WeightLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
		WeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		WeightLabel.setBounds(47, 409, 164, 34);
		add(WeightLabel);
		
		JTextPane scannedItemPane = new JTextPane();
		scannedItemPane.setEditable(false);
		scannedItemPane.setBounds(72, 11, 581, 336);
		add(scannedItemPane);
		
		JComboBox<String> potentialScanComboBox = new JComboBox<String>();
		//Adds all the barcoded items into the potential scannable combo box
		ArrayList<BarcodedItem> cBarcodedItems = new ArrayList<BarcodedItem>();
		for (int i = 1; i <= customer.shoppingCart.size(); i++) {
			potentialScanComboBox.addItem("Item " + i);
			cBarcodedItems.add((BarcodedItem) customer.shoppingCart.get(i - 1));
		}
		potentialScanComboBox.setBounds(365, 383, 91, 23);
		add(potentialScanComboBox);
		
		JButton scanButton = new JButton("Scan Item");
		scanButton.setVerticalAlignment(SwingConstants.TOP);
		scanButton.setToolTipText("Use drop down to select");
		scanButton.setFont(new Font("Georgia", Font.PLAIN, 12));
		// Action event when "Scan" button clicked
		AddItemScanned addItemScanned = new AddItemScanned(ProductDatabases.BARCODED_PRODUCT_DATABASE, doItYourselfStationAR.scanner);
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
		scanButton.setBounds(489, 383, 129, 23);
		add(scanButton);
		
		JComboBox<String> cardComboBox = new JComboBox<String>();
		for (Card card : customer.wallet.cards) {
			cardComboBox.addItem(card.cardholder + ":" + card.kind);
		}
		//Add customer cards to the combo box here
		cardComboBox.setBounds(150, 547, 101, 34);
		add(cardComboBox);
		
		JLabel Wallet = new JLabel("Cards in Wallet");
		Wallet.setForeground(Color.DARK_GRAY);
		Wallet.setFont(new Font("Georgia", Font.PLAIN, 13));
		Wallet.setHorizontalAlignment(SwingConstants.CENTER);
		Wallet.setBounds(150, 526, 101, 23);
		add(Wallet);
		
		JLabel lblItemsToScan = new JLabel("Items to Scan");
		lblItemsToScan.setHorizontalAlignment(SwingConstants.CENTER);
		lblItemsToScan.setForeground(Color.DARK_GRAY);
		lblItemsToScan.setFont(new Font("Georgia", Font.PLAIN, 13));
		lblItemsToScan.setBounds(359, 370, 97, 11);
		add(lblItemsToScan);
		
		JLabel pinLabel = new JLabel("Pin:");
		pinLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
		pinLabel.setBounds(489, 526, 50, 23);
		add(pinLabel);
		JTextField pinField = new JTextField();
		pinField.setBounds(541, 527, 65, 23);
		add(pinField);
		JButton PayButton = new JButton("Payment");
		PayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Action when "pay" button clicked
				try {
					CreditPayment newpay = new CreditPayment();
					newpay.setCard(customer.wallet.cards.get(cardComboBox.getSelectedIndex()));
					newpay.setReader(doItYourselfStationAR.cardReader);
					newpay.setCardIssuer(bank);
					newpay.insertCard(pinField.getText().intern()); // The intern() function will make sure the string is properly formatted.
					boolean flag = newpay.payForTotal(addItemScanned.getTotal());
					if (flag)
					{
						touchScreen.setVisible(false);
					}
					
				}
				catch (InvalidPINException e2)
				{
					JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Payment Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException e1) {
					// When Transcation Fails
					JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Payment Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		PayButton.setFont(new Font("Georgia", Font.PLAIN, 13));
		PayButton.setBounds(489, 560, 129, 51);
		add(PayButton);
	}

}
