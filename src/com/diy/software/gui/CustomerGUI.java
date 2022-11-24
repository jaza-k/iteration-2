package com.diy.software.gui;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.opeechee.Card;



public class CustomerGUI{
	private JFrame frame;
	
	private MainCustomerPanel panel;
	private PayPanelV2 payPanel;
	private JTabbedPane tabbedPane;
	private CreditPaymentPanel creditPanel;
	private CashPaymentPanel cashPanel;
	private DebitPaymentPanel debitPanel;
	private BaggingPanel baggingPanel;
	
	/**
	 * Constructor for CustomerGUI
	 * 
	 * @param doItYourselfStationLogic: The logic to be installed on the statio
	 * @param customer: The customer currently using the station
	 * @param screen: The DIY station touchscreen, contains the code to create the GUI frame
	 * @param bankingInfo: A hashmap of cards and their associated card issuers
	 */
	public CustomerGUI(DoItYourselfStationLogic doItYourselfStationLogic, Customer customer, TouchScreen screen, HashMap<Card, CardIssuer> bankingInfo){
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel = new MainCustomerPanel(customer, doItYourselfStationLogic, tabbedPane);
		payPanel = new PayPanelV2(tabbedPane);
		
		debitPanel = new DebitPaymentPanel(customer, doItYourselfStationLogic, tabbedPane, screen, bankingInfo);
		creditPanel = new CreditPaymentPanel(customer, doItYourselfStationLogic, tabbedPane, screen, bankingInfo);
		cashPanel = new CashPaymentPanel(customer, doItYourselfStationLogic, tabbedPane);
		baggingPanel = new BaggingPanel(customer, screen, doItYourselfStationLogic, tabbedPane);
		
		tabbedPane.add(panel);
		tabbedPane.add(payPanel);
		tabbedPane.add(creditPanel);
		tabbedPane.add(cashPanel);
		tabbedPane.add(debitPanel);
		tabbedPane.add(baggingPanel);
		tabbedPane.setEnabledAt(0, false);
	    tabbedPane.setEnabledAt(1, false);
	    tabbedPane.setEnabledAt(2, false);
	    tabbedPane.setEnabledAt(3, false);
	    tabbedPane.setEnabledAt(4, false);
	    tabbedPane.setEnabledAt(5, false);
	    
		frame = screen.getFrame();
		frame.setTitle("Customer GUI");
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(465, 500);
		frame.setLocation(700, 200);
		//frame.setLocationRelativeTo(null);
		screen.setVisible(true);
	}

}
