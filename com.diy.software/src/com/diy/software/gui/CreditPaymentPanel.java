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

public class CreditPaymentPanel extends JPanel {
	
	public CreditPaymentPanel(Customer customer, DoItYourselfStationAR doItYourselfStation, CardIssuer bank, TouchScreen screen, Cart cart) {
		
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
					boolean flag = newpay.payForTotal(cart.addItemScanned.getTotal());
					if (flag)
					{
						screen.setVisible(false);
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
