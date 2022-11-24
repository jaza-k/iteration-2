package com.diy.software.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;

public class CreditPaymentPanel extends JPanel {
	
	public CreditPaymentPanel(Customer customer, DoItYourselfStationLogic stationLogic, JTabbedPane tabbedPane, TouchScreen screen,  HashMap<Card, CardIssuer> bankingInfo) {
		setBackground(SystemColor.inactiveCaption);
		
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
		Wallet.setBounds(69, 355, 124, 15);
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
				// Action when payment being attempted
				try {
					
					CreditPayment newpay = new CreditPayment();
					newpay.setCard(customer.wallet.cards.get(cardComboBox.getSelectedIndex()));
					newpay.setReader(stationLogic.station.cardReader);
					newpay.setCardIssuer(bankingInfo.get(customer.wallet.cards.get(cardComboBox.getSelectedIndex())));
					//The intern() function will make sure the string is properly formatted.
					newpay.insertCard(pinField.getText().intern()); 
					boolean flag = newpay.payForTotal(stationLogic.scannerController.getTotal());
					
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
					// When Transaction Fails
					JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Payment Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		PayButton.setFont(new Font("Georgia", Font.PLAIN, 13));
		PayButton.setBounds(267, 403, 124, 49);
		add(PayButton);
		
		JTextPane scannedItemPane = new JTextPane();
		scannedItemPane.setEditable(false);
		scannedItemPane.setBounds(55, 22, 329, 304);
		add(scannedItemPane);
	}
}
