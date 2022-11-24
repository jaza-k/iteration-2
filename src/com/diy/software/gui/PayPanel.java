package com.diy.software.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;

public class PayPanel extends JPanel {
//The panel is going to have six buttons
	JButton cashButton;
	JButton creditButton;
	JButton debitButton;
	JButton backToScanButton;
	JTabbedPane tabbedPane;

	
	
	
	public PayPanel(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		
    	setLayout(new GridLayout(2,2));
    	addWidgets();	
    }
	
	
    private void addWidgets() {

    	cashButton = new JButton("Cash");
		cashButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(3);
				
			}

		});

    	creditButton = new JButton("Credit");
		creditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(2);
				
			}

		});
    	
    	debitButton = new JButton("Debit");
		debitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(4);
				
			}

		});
    	
    	
    	backToScanButton = new JButton("Back to Scan");
    	backToScanButton.addActionListener(e -> {
    		//call function  of "Permit Station Use", make sure the scanner, scale etc is turned on.
    	});
    	
    	
    	
    	add(cashButton);
    	add(creditButton);
    	add(debitButton);
    	add(backToScanButton);	
    	
    }
	
}
