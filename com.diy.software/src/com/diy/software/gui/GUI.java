package com.diy.software.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.jimmyselectronics.disenchantment.TouchScreen;

public class GUI{
	private JFrame frame;
	private IterationTwoPanel panel;
	/**
	 *
	 * @param doItYourselfStation: The current station
	 * @param wallet: Requires the wallet so the cards can be accessed and displayed onto the GUI
	 * @param curretCart: Requires all the items as an array list;items can be displayed to simulate a scan
	 */
	public GUI(DoItYourselfStationAR doItYourselfStationAR, TouchScreen touchScreen, Customer customer, CardIssuer bank) {
		frame = touchScreen.getFrame();

		panel = new IterationTwoPanel(customer, doItYourselfStationAR, touchScreen, bank);
		frame.setTitle("Customer GUI");

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(426, 500);
		frame.setLocationRelativeTo(null);
		touchScreen.setVisible(true);
	}
}