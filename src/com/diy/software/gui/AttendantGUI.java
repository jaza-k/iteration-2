package com.diy.software.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import com.diy.hardware.DoItYourselfStationAR;

/*
 * Set up attendant I/O
 */

public class AttendantGUI {
	JFrame AttendantFrame;
	JPanel AttendantPanel;
	JPanel Controls, MainMenu;
	
	JTextArea statText;
	
	JScrollPane scroller;
	
	//The panel is going to have six buttons
	JButton AddownBag;
	JButton AddItem;
	JButton RemoveItem;
	JButton PermitstationUse;
	JButton BlockstationUse;
	JButton Close;
	
	// SIZING
	JButton bigger, smaller, finish;
	
	// STATIONS (temporary solution)
	JButton s1, s2, s3, s4, s5, s6;
	
	int sized = 0;
	int issue = -1;	// represents the station having an issue
	//String selected = "Station ";
	
	
	int sxw = 400;	// starting x & w
	int syh = 200;	// starting y & h
	int x = 400;
	int y = 200;
	int w = 400;
	int h = 200;
	
	// Stations connected
	private DoItYourselfStationAR[] stations = {};
	
	
	
	
	public void addStation(JPanel sts) {	//DoItYourselfStationAR station) {
		
		s1 = new JButton("Station 1");
		s1.setFocusPainted(false);
		s1.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 1");
			issue = 1;

		});
		
		s2 = new JButton("Station 2");
		s2.setFocusPainted(false);
		s2.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 2");
			issue = 2;

		});
		
		s3 = new JButton("Station 3");
		s3.setFocusPainted(false);
		s3.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 3");
			issue = 3;

		});
		
		s4 = new JButton("Station 4");
		s4.setFocusPainted(false);
		s4.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 4");
			issue = 4;

		});
		
		s5 = new JButton("Station 5");
		s5.setFocusPainted(false);
		s5.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 5");
			issue = 5;

		});
		
		s6 = new JButton("Station 6");
		s6.setFocusPainted(false);
		s6.addActionListener(e -> {
			// open station...
			tabbedPane.setSelectedIndex(1);
			tabbedPane.setTitleAt(1,"Station 6");
			issue = 6;

		});
		
	    sts.add(s1);
	    sts.add(s2);
	    sts.add(s3);
	    sts.add(s4);
	    sts.add(s5);
	    sts.add(s6);
		
	}
	
	
	
	private void addWidgets(JPanel att) {

		AddownBag = new JButton("Add Own Bag");
		AddownBag.setHorizontalAlignment(SwingConstants.LEFT);
		AddownBag.setFocusPainted(false);
		AddownBag.addActionListener(e -> {
			// indicating the electronic scale to ignore weight discrepancy for next item

		});

		AddItem = new JButton("Add Item");
		AddItem.setHorizontalAlignment(SwingConstants.LEFT);
		AddItem.setFocusPainted(false);
		AddItem.addActionListener(e -> {
			//pop out new panel Call function of "Add Item"
			
		});
		
		RemoveItem = new JButton("Remove Item");
		RemoveItem.setHorizontalAlignment(SwingConstants.LEFT);
		RemoveItem.setFocusPainted(false);
		RemoveItem.addActionListener(e -> {
			//pop out new panel and call function  of "Remove Item"
			
		});
		
		PermitstationUse = new JButton("Permit Station Use");
		PermitstationUse.setHorizontalAlignment(SwingConstants.LEFT);
		PermitstationUse.setFocusPainted(false);
		PermitstationUse.addActionListener(e -> {
			//call function  of "Permit Station Use", make sure the scanner, scale etc is turned on.
			
		});
		
		BlockstationUse = new JButton("Block Station Use");
		BlockstationUse.setHorizontalAlignment(SwingConstants.LEFT);
		BlockstationUse.setFocusPainted(false);
		BlockstationUse.addActionListener(e -> {
			//call function of "Block Station Use", make sure the scanner, scale etc is enabled.

		});
		
	    Close = new JButton("Close");
		Close.setHorizontalAlignment(SwingConstants.LEFT);
		Close.setFocusPainted(false);
		Close.setBackground(Color.getHSBColor(80,40,10));
	    Close.addActionListener(e -> {
	    	// Return to main menu
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setTitleAt(1,"Station");
			AttendantFrame.validate();
			AttendantFrame.repaint();
			issue = -1;
	    	
		});
	    
	    att.add(AddownBag);
		att.add(AddItem);
		att.add(RemoveItem);
		att.add(PermitstationUse);
		att.add(BlockstationUse);
		att.add(Close);
	}
	
	private JTabbedPane tabbedPane;
	
		/*
		 * Test Function Below...
		 */
		/*
		public void printDimensions() {
	    	System.out.print("\nOur current dimensions are:");
	    	System.out.print("\nx: " + x + "\ny: " + y + "\nw: " + w + "\nh: " + h + "\n");
		}
		*/
	
		public void resizeGUI(int change) {
			if((w + change > 400) && (w + change < 600)) {
				sized += change;
				
				x = sxw - 48*sized;
				y = syh - 24*sized;
				w = sxw + 96*sized;
				h = syh - 12*sized;
				
				
				//x -= 50*change;
				//y -= 25*change;
				//w += 100*change;
				//h += 50*change;
			}
			AttendantFrame.setBounds(x,y,w,h+11*(sized+1)-7);
			Controls.setLocation(w*5/8 + 37*sized, 0);
			MainMenu.validate();
			AttendantFrame.validate();
			AttendantFrame.repaint();
			
		}
		
		public void sizingButtons() {
			
			// MAKE FRAME BIGGER
			bigger = new JButton("+");
			bigger.setFocusPainted(false);
			bigger.setBackground(Color.GREEN);
			// ADD BIGGER LISTENER
			bigger.addActionListener(e -> {
	    		resizeGUI(1);
	    	});
			
			// MAKE FRAME SMALLER
			smaller = new JButton("-");
			smaller.setFocusPainted(false);
			smaller.setBackground(Color.YELLOW);
			// ADD BIGGER LISTENER
			smaller.addActionListener(e -> {
	    		resizeGUI(-1);
	    	});
			
			// EXIT FRAME
			finish = new JButton("x");
			finish.setFocusPainted(false);
			finish.setBackground(Color.RED);
			// ADD FINISH LISTENER
			finish.addActionListener(e -> {
				AttendantFrame.setVisible(false);
		    	System.exit(0);	// to actually close everything
	    	});
			
			// INCLUDE
			Controls.add(bigger);
			Controls.add(smaller);
			Controls.add(finish);
			
			
		}
	    
	    public AttendantGUI(DoItYourselfStationAR[] sl) {
	    	
	    	stations = sl;
	    	// TEST BELOW LINE
	    	//System.out.print(stations.length);
	    	
	    	
	    	AttendantFrame = new JFrame("Attendant Panel");
	    	AttendantFrame.setUndecorated(true);
	    	
	    	AttendantFrame.setBounds(x,y,w,h+11*(sized+1)-8);
	    	//printDimensions();
	    	
	    	
	    	// INIT SIZING BUTTONS
	    	Controls = new JPanel();
	    	Controls.setLayout(new GridLayout(1,1));
	    	Controls.setBounds(w*5/8, 0, w*3/8, h/10);
	    	AttendantFrame.getContentPane().add(Controls);
	    	sizingButtons();
	    	
	    	
	    	// ADD ATTENDANT PANE
	    	AttendantPanel = new JPanel();
	    	AttendantPanel.setLayout(new GridLayout(6,1));
	    	
	    	// ADD MAIN MENU
	    	MainMenu = new JPanel();
	    	MainMenu.setLayout(new GridLayout(6,2));
	    	
	    	
	    	/*
	    	// MOVE THIS ELSEWHERE
	    	scroller = new JScrollPane(MainMenu);
	    	scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			//scroller.setBounds(x + 10, y + 60, w - 20, h - 70);
			//MainMenu.add(scroller);
			AttendantFrame.getContentPane().add(scroller);
			
			statText = new JTextArea();
			// BELOW: font is monospaced (!) and scales by width
			statText.setFont(new Font("MONOSPACED", Font.BOLD,this.x/40));
			// BELOW: clunky, but sets column via scaling, monospaced font
			statText.setEditable(false);
			scroller.setColumnHeaderView(statText);
			// MOVE ABOVE ELSEWHERE
			*/
	    	
	    	
	    	// REFACTOR THIS TO BE SCROLLABLE
	    	addStation(MainMenu);
	    	
	    	
	    	
	    	addWidgets(AttendantPanel);
	    	
	    	AttendantFrame.getContentPane().add(MainMenu, BorderLayout.CENTER);
	    	AttendantFrame.getContentPane().add(AttendantPanel, BorderLayout.CENTER);
	    	
	    	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    	AttendantFrame.getContentPane().add(tabbedPane, BorderLayout.NORTH);
	    	AttendantFrame.getContentPane().add(MainMenu, BorderLayout.CENTER);
	    	
	    	
	    	tabbedPane.addTab("Main Menu", MainMenu);
	    	tabbedPane.addTab("Station", AttendantPanel);
	    	
	    	AttendantFrame.setVisible(true);
	    	
	    	
	    	
	    }
	    
	    

}