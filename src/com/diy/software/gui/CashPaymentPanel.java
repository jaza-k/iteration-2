package com.diy.software.gui;

import ca.powerutility.NoPowerException;
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
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CashPaymentPanel extends JPanel {


    private double total;
    private DoItYourselfStationLogic stationLogic;
    private Payment newPay;
    private Customer customer;
    private JTabbedPane tabbedPane;
    private Currency currency;
    Locale ca = new Locale("en", "CA");
    NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(ca);


    public CashPaymentPanel(Customer customer, DoItYourselfStationLogic stationLogic, JTabbedPane tabbedPane) {
        this.customer = customer;
        this.stationLogic = stationLogic;
        this.tabbedPane = tabbedPane;
        // CURRENCY SHOULD IDEALLY BE PASSED INTO CONSTRUCTOR
        this.currency = Currency.getInstance("CAD");


        GridLayout experimentLayout = new GridLayout(4, 3);
        setLayout(experimentLayout);

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


        JLabel priceTotal = new JLabel();
        priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
        priceTotal.setBounds(175, 321, 144, 14);
        add(priceTotal);


        JLabel changeTotal = new JLabel();
        priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
        priceTotal.setBounds(175, 500, 144, 14);
        add(changeTotal);


        newPay = new Payment(stationLogic.station, stationLogic, total);
        CashPayment cashPayment = new CashPayment(stationLogic.station, stationLogic);


        double cost = newPay.checkoutTotal;
        priceTotal.setText("Total: " + (dollarFormat.format(cost)));
        changeTotal.setText("Change to be outputted: 0" + dollarFormat.getCurrency().getSymbol());


        // Combo-box for denominations setup
        JComboBox<String> billComboBox = new JComboBox<String>();
        JComboBox<String> coinComboBox = new JComboBox<String>();
        int[] billDenominations = stationLogic.station.banknoteDenominations;
        List<Long> coinDenominations = stationLogic.station.coinDenominations;
        for (int i = 0; i < billDenominations.length; i++) {
            billComboBox.addItem(billDenominations[i] + "");
        }
        for (int i = 0; i < coinDenominations.size(); i++) {
            coinComboBox.addItem(coinDenominations.get(i) + "");
        }
        billComboBox.setBounds(109, 349, 91, 23);
        add(billComboBox);
        coinComboBox.setBounds(109, 349, 91, 23);
        add(coinComboBox);


        // Insert Bill Button setup
        JButton payBillButton = new JButton("Insert Bill");
        payBillButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        payBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billDenomination = Integer.valueOf((String) billComboBox.getSelectedItem()); // Internally calls parseInt, returns Integer
                Banknote bill = new Banknote(currency, billDenomination);
                try {
                    stationLogic.station.banknoteInput.receive(bill);
                    double cost = newPay.checkoutTotal;
                    double change = 0;
                    if (cost < 0) {
                        change = -cost;
                        cost = 0;
                    }
                    priceTotal.setText("Remaining Cost: " + (dollarFormat.format(cost)));
                    changeTotal.setText("Change: " + (dollarFormat.format(cost)));
                } catch (DisabledException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Banknote Slot is disabled! Ask Attendant", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                } catch (TooMuchCashException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Remove Dangling Banknote", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                } catch (NoPowerException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Loss of power!", "Insertion Error", JOptionPane.ERROR_MESSAGE);

                    /////////////////// For testing////////////////////////////
                    stationLogic.station.plugIn();
                    stationLogic.station.turnOn();
                    ////////////////////////////////////////////////////////////
                }
            }

        });
        payBillButton.setBounds(237, 349, 91, 23);
        add(payBillButton);


        // Insert Coin Button setup
        JButton payCoinButton = new JButton("Insert Coin");
        payCoinButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        payCoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Long coinDenomination = Long.valueOf((String) coinComboBox.getSelectedItem());
                Coin coin = new Coin(currency, coinDenomination);
                try {
                    stationLogic.station.coinSlot.receive(coin);
                    double cost = newPay.checkoutTotal;
                    double change = 0;
                    if (cost < 0) {
                        change = -cost;
                        cost = 0;
                    }
                    priceTotal.setText("Remaining Cost: " + (dollarFormat.format(cost)));
                    changeTotal.setText("Change: " + (dollarFormat.format(cost)));
                } catch (DisabledException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Coin Slot is disabled! Ask Attendant", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                } catch (TooMuchCashException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Invalid Transaction!", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                } catch (NoPowerException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Loss of power!", "Insertion Error", JOptionPane.ERROR_MESSAGE);
                    stationLogic.station.plugIn();
                    stationLogic.station.turnOn();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });
        payCoinButton.setBounds(237, 349, 91, 23);
        add(payCoinButton);


        // Insert Coin Button setup
        JButton removeBanknoteButton = new JButton("Remove Dangling Banknote");
        removeBanknoteButton.setFont(new Font("Georgia", Font.PLAIN, 12));
        removeBanknoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean flag = stationLogic.station.banknoteInput.hasDanglingBanknote();
                if (flag) {
                    System.out.println(stationLogic.station.banknoteInput.removeDanglingBanknote());
                } else {
                    JOptionPane.showMessageDialog(getParent(), "No Banknote is Dangling!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        removeBanknoteButton.setBounds(237, 349, 20, 20);
        add(removeBanknoteButton);
    }


    public void updateTotal() {
        this.total = stationLogic.scannerController.getTotal();
    }

}
		

