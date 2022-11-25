package com.diy.software.gui;

import com.diy.software.AttendantStationLogic;
import com.diy.software.DoItYourselfStationLogic;

import javax.swing.*;
import java.awt.*;

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
    JButton ApproveDiscrepancy;
    JButton BlockstationUse;
    JButton Close;

    // SIZING
    JButton bigger, smaller, finish;

    // STATIONS (temporary solution)
    JButton s1, s2, s3, s4, s5, s6;	// SHOULD HAVE DONE AS ARRAY


    //int issue = -1;	// represents the station having an issue
    //String selected = "Station ";

    // sID which is currently selected
    int sID = -1;    // should be nothing or -1 by default
    int[] sIssues;	// issues per station, each -1 if not init, 0 if init & good


    // sized = scaling tracker
    int sized = 0;
    // s_ = starting_
    int sx = 160;    // starting x
    int sy = 200;    // starting y
    int sw = 400;    // starting w
    int sh = 200;    // starting h
    int x = 160;
    int y = 200;
    int w = 400;
    int h = 200;

    // Stations connected
    private DoItYourselfStationLogic[] stations = {};

    public void updateIssues() {
        sIssues = AttendantStationLogic.getInstance().getIssues();
        // update/refresh GUI based on above line
        for(int i = 0; i < sIssues.length; i++) {
        	//System.out.print("\nStation " + i + " status is: " + sIssues[i]);
        }
        
        if(sIssues[0] != 0) s1.setBackground(Color.getHSBColor(160, 100, 60)); else s1.setBackground(null);
        if(sIssues[1] != 0) s2.setBackground(Color.getHSBColor(160, 100, 60)); else s2.setBackground(null);
        if(sIssues[2] != 0) s3.setBackground(Color.getHSBColor(160, 100, 60)); else s3.setBackground(null);
        if(sIssues[3] != 0) s4.setBackground(Color.getHSBColor(160, 100, 60)); else s4.setBackground(null);
        if(sIssues[4] != 0) s5.setBackground(Color.getHSBColor(160, 100, 60)); else s5.setBackground(null);
        if(sIssues[5] != 0) s6.setBackground(Color.getHSBColor(160, 100, 60)); else s6.setBackground(null);
       
        
    }

    public void receiptPrinterPaperLowPopUp() {
        // TEST: KEPT AS DUMMY, BUT NOT NEEDED ATM
        System.out.print("\nWhere is Michael Scott when you need him??\n");
    }


    public void receiptPrinterInkLowPopUp() {
        // TEST: KEPT AS DUMMY, BUT NOT NEEDED ATM
        System.out.print("\nOut of ink?? I guess we are bankrupt...\n");
    }

    public void addStation(JPanel sts) {    //DoItYourselfStationAR station) {

        s1 = new JButton("Station 1");
        s1.setFocusPainted(false);
        s1.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 1");
            sID = 0;

        });

        s2 = new JButton("Station 2");
        s2.setFocusPainted(false);
        s2.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 2");
            sID = 1;

        });

        s3 = new JButton("Station 3");
        s3.setFocusPainted(false);
        s3.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 3");
            sID = 2;

        });

        s4 = new JButton("Station 4");
        s4.setFocusPainted(false);
        s4.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 4");
            sID = 3;

        });

        s5 = new JButton("Station 5");
        s5.setFocusPainted(false);
        s5.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 5");
            sID = 4;

        });

        s6 = new JButton("Station 6");
        s6.setFocusPainted(false);
        s6.addActionListener(e -> {
            // open station...
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setTitleAt(1, "Station 6");
            sID = 5;

        });

        sts.add(s1);
        sts.add(s2);
        sts.add(s3);
        sts.add(s4);
        sts.add(s5);
        sts.add(s6);

    }


    private void addWidgets(JPanel att) {

        AddownBag = new JButton("Approve Bags");
        AddownBag.setHorizontalAlignment(SwingConstants.LEFT);
        AddownBag.setFocusPainted(false);
        AddownBag.addActionListener(e -> {
            // indicating the electronic scale to ignore weight discrepancy for next item

            // assume yes FOR NOW
            if (sID != -1) {
                AttendantStationLogic.getInstance().attendantDecision(sID, true);
            }
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

        ApproveDiscrepancy = new JButton("Approve Discrepancy");
        ApproveDiscrepancy.setHorizontalAlignment(SwingConstants.LEFT);
        ApproveDiscrepancy.setFocusPainted(false);
        ApproveDiscrepancy.addActionListener(e -> {
            //pop out new panel and call function  of "Remove Item"
            if (sID != -1) {
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
                AttendantStationLogic.getInstance().attendantDecision(sID, true);
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
            }
        });

        PermitstationUse = new JButton("Permit Station Use");
        PermitstationUse.setHorizontalAlignment(SwingConstants.LEFT);
        PermitstationUse.setFocusPainted(false);
        PermitstationUse.addActionListener(e -> {
            //call function  of "Permit Station Use", make sure the scanner, scale etc is turned on.
            if (sID != -1) {
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
                AttendantStationLogic.getInstance().getStationLogic(sID).unblock(AttendantStationLogic.getInstance().getStationLogic(sID).getStation());
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
            }
        });

        BlockstationUse = new JButton("Block Station Use");
        BlockstationUse.setHorizontalAlignment(SwingConstants.LEFT);
        BlockstationUse.setFocusPainted(false);
        BlockstationUse.addActionListener(e -> {
            //call function of "Block Station Use", make sure the scanner, scale etc is enabled.
            if (sID != -1) {
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
                AttendantStationLogic.getInstance().getStationLogic(sID).block(AttendantStationLogic.getInstance().getStationLogic(sID).getStation());
                //System.out.print("\nStation " + sID + " status is currently: " + AttendantStationLogic.getInstance().getStationLogic(sID).getStatus());
            }
        });

        Close = new JButton("Close");
        Close.setHorizontalAlignment(SwingConstants.LEFT);
        Close.setFocusPainted(false);
        Close.setBackground(Color.getHSBColor(80, 40, 10));
        Close.addActionListener(e -> {
            // Return to main menu
            tabbedPane.setSelectedIndex(0);
            tabbedPane.setTitleAt(1, "Station");
            AttendantFrame.validate();
            AttendantFrame.repaint();
            sID = -1;    // no station selected
        });

        att.add(AddownBag);
        att.add(AddItem);
        att.add(RemoveItem);
        att.add(PermitstationUse);
        att.add(ApproveDiscrepancy);
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
        if ((w + change > sw) && (w + change < sw + (96 * 2))) {
            sized += change;

            x = sx - 48 * sized;
            //y = sy - 24*sized;
            w = sw + 96 * sized;
            //h = sh + 24*sized;

        }
        //AttendantFrame.setBounds(x,y,w,h+11*(sized+1)-7);

        AttendantFrame.setBounds(x, y, w, h + 2);
        Controls.setLocation(w * 5 / 8 + 37 * sized, 0);
        tabbedPane.setBounds(x, y, w, h);
        MainMenu.setBounds(x, y, w, h);
        AttendantPanel.setBounds(x, y, w, h);

        MainMenu.validate();
        MainMenu.repaint();
        AttendantPanel.validate();
        AttendantPanel.repaint();
        tabbedPane.validate();
        tabbedPane.repaint();
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
            System.exit(0);    // to actually close everything
        });

        // INCLUDE
        Controls.add(bigger);
        Controls.add(smaller);
        Controls.add(finish);


    }

    public AttendantGUI(DoItYourselfStationLogic[] sl) {

        stations = sl;
        // TEST BELOW LINE
        //System.out.print(stations.length);


        AttendantFrame = new JFrame("Attendant Panel");
        AttendantFrame.setUndecorated(true);

        AttendantFrame.setBounds(sx, sy, w, h + 2);
        //printDimensions();


        // INIT SIZING BUTTONS
        Controls = new JPanel();
        Controls.setLayout(new GridLayout(1, 1));
        Controls.setBounds(w * 5 / 8, 0, w * 3 / 8, h / 10);
        AttendantFrame.getContentPane().add(Controls);
        sizingButtons();


        // ADD ATTENDANT PANE
        AttendantPanel = new JPanel();
        AttendantPanel.setLayout(new GridLayout(6, 1));

        // ADD MAIN MENU
        MainMenu = new JPanel();
        MainMenu.setLayout(new GridLayout(6, 2));
	    	
	    	
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