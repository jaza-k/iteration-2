package com.diy.software.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayPanel extends JPanel {
    //The panel is going to have six buttons
    JButton cashButton;
    JButton creditButton;
    JButton debitButton;
    JButton backToScanButton;
    JTabbedPane tabbedPane;


    public PayPanel(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;

        setLayout(new GridLayout(2, 2));
        addWidgets();
    }


    private void addWidgets() {

        cashButton = new JButton("Cash");
        cashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to PayPanel
                tabbedPane.setSelectedIndex(3);

            }

        });

        creditButton = new JButton("Credit");
        creditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to PayPanel
                tabbedPane.setSelectedIndex(2);

            }

        });

        debitButton = new JButton("Debit");
        debitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to PayPanel
                tabbedPane.setSelectedIndex(4);

            }

        });


        backToScanButton = new JButton("Back to Scan");
        backToScanButton.addActionListener(e -> {
            //call function  of "Permit Station Use", make sure the scanner, scale etc is turned on.
        });


        add(cashButton);
        add(creditButton);
        add(debitButton);
        add(backToScanButton);

    }

}
