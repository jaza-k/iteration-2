package com.diy.software.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/*
 * Set up attendant I/O
 */
	
public class AttendantGUI {
	JFrame AttendantFrame;
	JPanel AttendantPanel;
//The panel is going to have six buttons
	JButton AddownBag;
	JButton AddItem;
	JButton RemoveItem;
	JButton PermitstationUse;
	JButton BlockstationUse;
	JButton Exit;
	private JTabbedPane tabbedPane;
	    
	    public AttendantGUI() {
	    	AttendantFrame = new JFrame("Attendant Panel");
	    	AttendantPanel = new JPanel();
	    	AttendantPanel.setLayout(new GridLayout(2,3));
	    	
	    	
	    	addWidgets();
	    	
	    	AttendantFrame.getContentPane().add(AttendantPanel, BorderLayout.CENTER);
	    	
	    	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    	AttendantFrame.getContentPane().add(tabbedPane, BorderLayout.NORTH);
	    	AttendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	AttendantFrame.pack();
	    	tabbedPane.addTab("Station 1", AttendantPanel);
	    	AttendantFrame.setVisible(true);
	    	
	    	
	    	
	    	
	    }
	    private void addWidgets() {

	    	AddownBag = new JButton("Add Own Bag");
	    	AddownBag.addActionListener(e -> {
	    		// indicating the electronic scale to ignore weight discrepancy for next item

	    	});

	    	AddItem = new JButton("Add Item");
	    	AddItem.addActionListener(e -> {
	    		//pop out new panel Call function of "Add Item"
	    	});
	    	
	    	RemoveItem = new JButton("Remove Item");
	    	RemoveItem.addActionListener(e -> {
	    		//pop out new panel and call function  of "Remove Item"
	    	});
	    	
	    	PermitstationUse = new JButton("Permit Station Use");
	    	PermitstationUse.addActionListener(e -> {
	    		//call function  of "Permit Station Use", make sure the scanner, scale etc is turned on.
	    	});
	    	
	    	BlockstationUse = new JButton("Block Station Use");
	    	BlockstationUse.addActionListener(e -> {
	    		//call function of "Block Station Use", make sure the scanner, scale etc is enabled.

	    	});
	    	
		    Exit = new JButton("Exit");
		    Exit.addActionListener(e -> {
		    		//Exit the panel
		    	AttendantFrame.setVisible(false);
		    	
	    	});	    	
	    	AttendantPanel.add(AddownBag);
	    	AttendantPanel.add(AddItem);
	    	AttendantPanel.add(RemoveItem);
	    	AttendantPanel.add(PermitstationUse);
	    	AttendantPanel.add(BlockstationUse);
	    	AttendantPanel.add(Exit);
	    	
	    }
}