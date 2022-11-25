<<<<<<< HEAD
package com.diy.software.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayPanelV2 extends JPanel {
    //The panel is going to have six buttons
    JButton cashButton;
    JButton creditButton;
    JButton debitButton;
    JButton backToScanButton;
    JTabbedPane tabbedPane;

    public PayPanelV2(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;


        addWidgets();

    }


    private void addWidgets() {

        cashButton = new JButton("Cash");
        cashButton.setBackground(SystemColor.inactiveCaption);
        cashButton.setBounds(0, 0, 225, 301);
        cashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(4);
            }
        });

        creditButton = new JButton("Credit");
        creditButton.setBackground(SystemColor.inactiveCaption);
        creditButton.setBounds(225, 0, 240, 301);
        creditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(3);
            }
        });

        debitButton = new JButton("Debit");
        debitButton.setBackground(SystemColor.inactiveCaption);
        debitButton.setBounds(0, 300, 225, 200);
        debitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(5);
            }
        });

        backToScanButton = new JButton("Back to Scanning");
        backToScanButton.setBackground(new Color(240, 128, 128));
        backToScanButton.setBounds(225, 300, 240, 200);
        backToScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(0);
            }
        });
        setLayout(null);

        add(cashButton);
        add(creditButton);
        add(debitButton);
        add(backToScanButton);
    }

=======
package com.diy.software.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayPanelV2 extends JPanel {
    //The panel is going to have six buttons
    JButton cashButton;
    JButton creditButton;
    JButton debitButton;
    JButton backToScanButton;
    JTabbedPane tabbedPane;

    public PayPanelV2(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;


        addWidgets();

    }


    private void addWidgets() {

        cashButton = new JButton("Cash");
        cashButton.setBackground(SystemColor.inactiveCaption);
        cashButton.setBounds(0, 0, 225, 301);
        cashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(4);
            }
        });

        creditButton = new JButton("Credit");
        creditButton.setBackground(SystemColor.inactiveCaption);
        creditButton.setBounds(225, 0, 240, 301);
        creditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(3);
            }
        });

        debitButton = new JButton("Debit");
        debitButton.setBackground(SystemColor.inactiveCaption);
        debitButton.setBounds(0, 300, 225, 200);
        debitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(5);
            }
        });

        backToScanButton = new JButton("Back to Scanning");
        backToScanButton.setBackground(new Color(240, 128, 128));
        backToScanButton.setBounds(225, 300, 240, 200);
        backToScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(0);
            }
        });
        setLayout(null);

        add(cashButton);
        add(creditButton);
        add(debitButton);
        add(backToScanButton);
    }

>>>>>>> main
}