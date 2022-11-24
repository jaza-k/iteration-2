package com.diy.software.gui;


import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.disenchantment.TouchScreen;

public class BaggingPanel extends JPanel {
//	private JTextField numberOfBags;

	/**
	 * Create the panel.
	 */
	public BaggingPanel(Customer customer, TouchScreen screen, DoItYourselfStationLogic doItYourselfStationLogic, JTabbedPane tabbedPane) {
		setBackground(SystemColor.inactiveCaption);
		setLayout(null);
		
		JButton btnNewButton = new JButton("Continue to Payment");
		btnNewButton.setFont(new Font("Georgia", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		btnNewButton.setBounds(203, 384, 189, 39);
		add(btnNewButton);
		
		JTextPane scannedItemPane = new JTextPane();
		scannedItemPane.setEditable(false);
		scannedItemPane.setBounds(63, 51, 329, 129);
		add(scannedItemPane);
		
//		JButton buyBag = new JButton("Buy Bags");
//		buyBag.setBounds(267, 224, 114, 23);
//		add(buyBag);
//
//		numberOfBags = new JTextField();
//		numberOfBags.setBounds(95, 225, 106, 20);
//		add(numberOfBags);
//		numberOfBags.setColumns(10);
		
//		JLabel lblNewLabel = new JLabel("Number of Bags");
//		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
//		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		lblNewLabel.setBounds(84, 209, 122, 14);
//		add(lblNewLabel);

	}
}
