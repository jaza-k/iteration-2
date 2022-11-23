package com.diy.software.gui;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.diy.simulation.Customer;
//import com.diy.software.payment.CashPaymentPanel;
//import com.diy.software.payment.DebitPaymentPanel;
//import com.diy.software.scansoft.AddItemScanned;


public class GUI{
	private JFrame frame;
	private CustomJPanel panel;
	private PayPanel payPanel;
	private JTabbedPane tabbedPane;
	//private CreditPaymentPanel creditPanel;
	//private CashPaymentPanel cashPanel;
	//private DebitPaymentPanel debitPanel;
	/**
	 * 
	 * @param doItYourselfStation: The current station
	 * @param wallet: Requires the wallet so the cards can be accessed and displayed onto the GUI
	 * @param curretCart: Requires all the items as an array list;items can be displayed to simulate a scan
	 * @param screen: Contains the code to create the frame.
	 */
	
	/*
	public GUI(DoItYourselfStationAR doItYourselfStation, Customer customer, CardIssuer bank, TouchScreen screen) {
		
		AddItemScanned addItemScanned = new AddItemScanned(ProductDatabases.BARCODED_PRODUCT_DATABASE, doItYourselfStation.scanner);
		Cart cart = new Cart(addItemScanned);
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel = new CustomJPanel(customer, doItYourselfStation, bank, screen, cart, tabbedPane);
		payPanel = new PayPanel(tabbedPane);
		creditPanel = new CreditPaymentPanel(customer, doItYourselfStation, bank, screen, cart);
		cashPanel = new CashPaymentPanel(customer, doItYourselfStation, bank, screen, cart);
		debitPanel = new DebitPaymentPanel(customer, doItYourselfStation, bank, screen, cart);
		
		
		
		tabbedPane.add(panel);
		tabbedPane.add(payPanel);
		tabbedPane.add(creditPanel);
		tabbedPane.add(cashPanel);
		tabbedPane.add(debitPanel);
	    tabbedPane.setEnabledAt(1, false);
	    tabbedPane.setEnabledAt(0, false);
	    tabbedPane.setEnabledAt(2, false);
	    tabbedPane.setEnabledAt(3, false);
	    tabbedPane.setEnabledAt(4, false);
	    
	    
		frame = screen.getFrame();
		frame.setTitle("Customer GUI");
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(426, 500);
		frame.setLocationRelativeTo(null);
		screen.setVisible(true);
	}
	*/

}
