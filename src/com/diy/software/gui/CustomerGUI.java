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
	private UnexpectedWeight unpectedWeightPanel;
	
	/**
	 * 
	 * @param doItYourselfStation: The current station
	 * @param wallet: Requires the wallet so the cards can be accessed and displayed onto the GUI
	 * @param curretCart: Requires all the items as an array list;items can be displayed to simulate a scan
	 * @param screen: Contains the code to create the frame.
	 */
	
	public CustomerGUI(DoItYourselfStationLogic doItYourselfStationLogic, Customer customer, TouchScreen screen, HashMap<Card, CardIssuer> bankingInfo){
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel = new MainCustomerPanel(customer, doItYourselfStationLogic, tabbedPane);
		payPanel = new PayPanelV2(tabbedPane);
		
		
		//creditPanel = new CreditPaymentPanel(customer, doItYourselfStation, bank, screen, cart);
		debitPanel = new DebitPaymentPanel(customer, doItYourselfStationLogic, tabbedPane);
		creditPanel = new CreditPaymentPanel(customer, doItYourselfStationLogic, tabbedPane, screen, bankingInfo);
		cashPanel = new CashPaymentPanel(customer, doItYourselfStationLogic, tabbedPane);
		baggingPanel = new BaggingPanel(customer, screen, doItYourselfStationLogic, tabbedPane);
		unpectedWeightPanel = new UnexpectedWeight();
		
		tabbedPane.add(panel);
		tabbedPane.add(payPanel);
		tabbedPane.add(creditPanel);
		tabbedPane.add(cashPanel);
		tabbedPane.add(debitPanel);
		tabbedPane.add(baggingPanel);
		tabbedPane.add(unpectedWeightPanel);
		tabbedPane.setEnabledAt(0, false);
	    tabbedPane.setEnabledAt(1, false);
	    tabbedPane.setEnabledAt(2, false);
	    tabbedPane.setEnabledAt(3, false);
	    tabbedPane.setEnabledAt(4, false);
	    tabbedPane.setEnabledAt(5, false);
	    tabbedPane.setEnabledAt(6, false);
	    
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
