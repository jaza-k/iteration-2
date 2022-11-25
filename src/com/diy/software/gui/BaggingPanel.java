package com.diy.software.gui;


import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.disenchantment.TouchScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaggingPanel extends JPanel {

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
                tabbedPane.setSelectedIndex(2);
            }
        });
        btnNewButton.setBounds(210, 391, 189, 39);
        add(btnNewButton);

    }
}
