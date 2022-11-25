package com.diy.software.gui;

import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.payment.CashPayment;
import com.diy.software.payment.Payment;
import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.coin.Coin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Currency;
import java.util.List;

public class CashPaymentPanel extends JPanel {


    private double total;
    private DoItYourselfStationLogic stationLogic;
    private Payment newPay;
    Customer customer;
    JTabbedPane tabbedPane;

    public CashPaymentPanel(Customer customer, DoItYourselfStationLogic stationLogic, JTabbedPane tabbedPane) {
        this.customer = customer;
        this.stationLogic = stationLogic;
        this.tabbedPane = tabbedPane;


        //newPay = new Payment(stationLogic.station, stationLogic.scannerController.getTotal());


        JButton beginCashPaymentButton = new JButton("Begin Inserting");
        beginCashPaymentButton.setFont(new Font("Georgia", Font.PLAIN, 12));


        beginCashPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(beginCashPaymentButton);
                beginPayment();
            }
        });

        beginCashPaymentButton.setBounds(237, 349, 91, 23);
        add(beginCashPaymentButton);

    }


    public void beginPayment() {

        updateTotal();


        JLabel priceTotal = new JLabel("Current Total: $");
        priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
        priceTotal.setBounds(175, 321, 144, 14);
        add(priceTotal);

        newPay = new Payment(stationLogic.station, stationLogic, total);
        CashPayment cashPayment = new CashPayment(stationLogic.station, stationLogic);


        double cost = newPay.checkoutTotal;
        priceTotal.setText("Remaining Cost:" + cost);


        JComboBox<String> billComboBox = new JComboBox<String>();
        JComboBox<String> coinComboBox = new JComboBox<String>();


        //Adds all the barcoded items into the potential scannable combo box


        // should be removed and instead done elsewhere eg-stationLogic


        int[] billDenominations = stationLogic.station.banknoteDenominations;

        List<Long> coinDenominations = stationLogic.station.coinDenominations;


        for (int i = 0; i < billDenominations.length; i++) {
            // Ideally $ should be replaced with Currency
            billComboBox.addItem(billDenominations[i] + "");
        }


        for (int i = 0; i < coinDenominations.size(); i++) {
            // Ideally $ should be replaced with Currency
            coinComboBox.addItem(coinDenominations.get(i) + "");
        }


        billComboBox.setBounds(109, 349, 91, 23);
        add(billComboBox);

        coinComboBox.setBounds(109, 349, 91, 23);
        add(coinComboBox);


        JButton payBillButton = new JButton("Insert Bill");
        payBillButton.setFont(new Font("Georgia", Font.PLAIN, 12));


        payBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billDenomination = Integer.valueOf((String) billComboBox.getSelectedItem()); // Internally calls parseInt, returns Integer
                // cad should not be hard-coded
                /////////////////////////////////////////// FIX /////////////////////////////
                Banknote bill = new Banknote(Currency.getInstance("CAD"), billDenomination);
                ///////////////////////////////////////////////////////////////////////////
                try {
                    stationLogic.station.banknoteInput.receive(bill);
                    double cost = newPay.checkoutTotal;
                    priceTotal.setText("Remaining Cost:" + cost);
                } catch (DisabledException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (TooMuchCashException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });
        payBillButton.setBounds(237, 349, 91, 23);
        add(payBillButton);

        JButton payCoinButton = new JButton("Insert Coin");
        payCoinButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        payCoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Long coinDenomination = Long.valueOf((String) coinComboBox.getSelectedItem()); // Internally calls parseInt, returns Integer
                // cad should not be hard-coded
                /////////////////////////////////////////// FIX /////////////////////////////
                Coin coin = new Coin(Currency.getInstance("CAD"), coinDenomination);
                ///////////////////////////////////////////////////////////////////////////
                try {
                    stationLogic.station.coinSlot.receive(coin);
                    double cost = newPay.checkoutTotal;
                    priceTotal.setText("Remaining Cost:" + cost);
                } catch (DisabledException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (TooMuchCashException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });
        payCoinButton.setBounds(237, 349, 91, 23);
        add(payCoinButton);
    }


    public void updateTotal() {
        this.total = stationLogic.scannerController.getTotal();
    }

}
		

