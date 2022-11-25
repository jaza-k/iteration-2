package com.diy.software.gui;

import com.jimmyselectronics.disenchantment.TouchScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentSuccessPanel extends JPanel {
    //The panel is going to have six buttons
    JButton doneButton;
    JButton backToScanButton;
    JTabbedPane tabbedPane;
    TouchScreen screen;

    public PaymentSuccessPanel(JTabbedPane tabbedPane, TouchScreen screen) {
        this.tabbedPane = tabbedPane;
        this.screen = screen;
        addWidgets();
    }


    private void addWidgets() {
        doneButton = new JButton("Done");
        doneButton.setBackground(new Color(128, 240, 128));
        doneButton.setBounds(0, 0, 480, 274);
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                screen.setVisible(false);
            }
        });

        backToScanButton = new JButton("Back to Scanning");
        backToScanButton.setBackground(new Color(240, 128, 128));
        backToScanButton.setBounds(0, 274, 480, 274);
        backToScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // switch to appropriate panel after selection
                tabbedPane.setSelectedIndex(0);
            }
        });
        setLayout(null);

        add(doneButton);
        add(backToScanButton);
    }

}