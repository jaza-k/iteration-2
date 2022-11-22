package com.diy.software.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;

public class GUI{
	private JFrame frame;
	private CustomJPanel panel;
	/**
	 *
	 * @param doItYourselfStation: The current station
	 * @param wallet: Requires the wallet so the cards can be accessed and displayed onto the GUI
	 * @param curretCart: Requires all the items as an array list;items can be displayed to simulate a scan
	 */


	public GUI(DoItYourselfStationAR doItYourselfStation, Customer customer, CardIssuer bank) {
//        frame = doItYourselfStation.touchScreen.getFrame();

		panel = new CustomJPanel(customer, doItYourselfStation, bank);
		frame.setTitle("Customer GUI");

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(426, 500);
		frame.setLocationRelativeTo(null);
//        doItYourselfStation.touchScreen.setVisible(true);
	}
}