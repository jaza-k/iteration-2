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
        setBackground(Color.PINK);
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Please wait for the attendant to approve your request!");
        lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(46, 274, 363, 22);
        add(lblNewLabel);

    }
}
