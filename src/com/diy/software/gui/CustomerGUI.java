package com.diy.software.gui;

import com.diy.hardware.external.CardIssuer;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.opeechee.Card;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;


public class CustomerGUI {
    private JFrame frame;

    private MainCustomerPanel panel;
    private PayPanelV2 payPanel;
    private JTabbedPane tabbedPane;
    private CreditPaymentPanel creditPanel;
    private CashPaymentPanel cashPanel;
    private DebitPaymentPanel debitPanel;
    private BaggingPanel baggingPanel;
    private UnexpectedWeight unexpectedWeightPanel;
    private PaymentSuccessPanel paymentSuccessPanel;

    /**
     * Constructor for CustomerGUI
     *
     * @param bagBarcode
     * @param doItYourselfStationLogic: The logic to be installed on the statio
     * @param customer:                 The customer currently using the station
     * @param screen:                   The DIY station touchscreen, contains the code to create the GUI frame
     * @param bankingInfo:              A hashmap of cards and their associated card issuers
     */
    public CustomerGUI(DoItYourselfStationLogic doItYourselfStationLogic, Customer customer, TouchScreen screen, HashMap<Card, CardIssuer> bankingInfo, Barcode bagBarcode) {

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        panel = new MainCustomerPanel(customer, doItYourselfStationLogic, tabbedPane, bagBarcode);
        payPanel = new PayPanelV2(tabbedPane);
        debitPanel = new DebitPaymentPanel(customer, doItYourselfStationLogic, tabbedPane, screen, bankingInfo);
        creditPanel = new CreditPaymentPanel(customer, doItYourselfStationLogic, tabbedPane, bankingInfo);
        cashPanel = new CashPaymentPanel(customer, doItYourselfStationLogic, tabbedPane);
        baggingPanel = new BaggingPanel(customer, screen, doItYourselfStationLogic, tabbedPane);
        unexpectedWeightPanel = new UnexpectedWeight();
        paymentSuccessPanel = new PaymentSuccessPanel(tabbedPane, screen);

        tabbedPane.add("Scan Items", panel);
        tabbedPane.add("Bags", baggingPanel);
        tabbedPane.add("Payment", payPanel);
        tabbedPane.add("Credit", creditPanel);
        tabbedPane.add("Cash", cashPanel);
        tabbedPane.add("Debit", debitPanel);
        tabbedPane.add("Unexpected Weight", unexpectedWeightPanel);
        tabbedPane.add("Payment Success", paymentSuccessPanel);
        tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(3, false);
        tabbedPane.setEnabledAt(4, false);
        tabbedPane.setEnabledAt(5, false);
        tabbedPane.setEnabledAt(6, false);
        tabbedPane.setEnabledAt(7, false);

        frame = screen.getFrame();
        frame.setTitle("Customer GUI");
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(465, 575);
        frame.setLocation(700, 200);
        //frame.setLocationRelativeTo(null);
        screen.setVisible(true);
    }


    public void setTabbedFocus(int t) {
        tabbedPane.setSelectedIndex(0);
    }
}
