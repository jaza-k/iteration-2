package com.diy.software.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.SystemColor;

public class PayPanel extends JPanel {
//The panel is going to have six buttons
	JButton cashButton;
	JButton creditButton;
	JButton debitButton;
	JButton backToScanButton;
	JTabbedPane tabbedPane;

	public PayPanel(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
    	addWidgets();	
    }
	
	
    private void addWidgets() {

    	cashButton = new JButton("Cash");
    	cashButton.setBackground(SystemColor.inactiveCaption);
    	cashButton.setBounds(0, 0, 225, 301);
		cashButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(3);
			}
		});

    	creditButton = new JButton("Credit");
    	creditButton.setBackground(SystemColor.inactiveCaption);
    	creditButton.setBounds(225, 0, 240, 301);
		creditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(2);
			}
		});
    	
    	debitButton = new JButton("Debit");
    	debitButton.setBackground(SystemColor.inactiveCaption);
    	debitButton.setBounds(0, 300, 225, 200);
		debitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(4);
			}
		});
    	
    	backToScanButton = new JButton("Back to Scanning");
    	backToScanButton.setBackground(new Color(240, 128, 128));
    	backToScanButton.setBounds(225, 300, 240, 200);
    	backToScanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch to PayPanel
				tabbedPane.setSelectedIndex(0);
			}
    	});
    	setLayout(null);
    	
    	add(cashButton);
    	add(creditButton);
    	add(debitButton);
    	add(backToScanButton);	
    }
	
}
