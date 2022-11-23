package com.diy.software.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;
import java.awt.SystemColor;
import javax.swing.JTextPane;

public class CreditPaymentPanel extends JPanel {
	
	public CreditPaymentPanel(Customer customer, DoItYourselfStationAR doItYourselfStation, CardIssuer bank, TouchScreen screen, Cart cart) {
		setBackground(SystemColor.inactiveCaption);
		setToolTipText("");
		
		JComboBox<String> cardComboBox = new JComboBox<String>();
		for (Card card : customer.wallet.cards) {
			cardComboBox.addItem(card.cardholder + ":" + card.kind);
		}
		setLayout(null);
		//Add customer cards to the combo box here
		cardComboBox.setBounds(79, 370, 114, 22);
		add(cardComboBox);
		
		JLabel Wallet = new JLabel("Cards in Wallet");
		Wallet.setForeground(Color.DARK_GRAY);
		Wallet.setFont(new Font("Georgia", Font.PLAIN, 13));
		Wallet.setHorizontalAlignment(SwingConstants.CENTER);
		Wallet.setBounds(69, 344, 124, 15);
		add(Wallet);
			
		JLabel pinLabel = new JLabel("Pin:");
		pinLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
		pinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pinLabel.setBounds(257, 359, 43, 33);
		add(pinLabel);
		JTextField pinField = new JTextField();
		pinField.setToolTipText("pin");
		pinField.setBounds(298, 366, 86, 20);
		add(pinField);
		JButton PayButton = new JButton("Make Payment");
		PayButton.setBackground(new Color(143, 188, 143));
		
		PayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Action when "pay" button clicked
				try {
					
					CreditPayment newpay = new CreditPayment();
					newpay.setCard(customer.wallet.cards.get(cardComboBox.getSelectedIndex()));
					newpay.setReader(doItYourselfStation.cardReader);
					newpay.setCardIssuer(bank);
					newpay.insertCard(pinField.getText().intern()); //The intern() function will make sure the string is properly formatted.
					//System.out.println("Card successfully inserted");
					boolean flag = newpay.payForTotal(cart.addItemScanned.getTotal());
					if (flag)
					{
						screen.setVisible(false);
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
		PayButton.setBounds(267, 403, 124, 49);
		add(PayButton);
		
		JTextPane scannedItemPane = new JTextPane();
		scannedItemPane.setEditable(false);
		scannedItemPane.setBounds(55, 11, 329, 304);
		add(scannedItemPane);
	}
	
}
