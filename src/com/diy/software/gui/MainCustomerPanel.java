package com.diy.software.gui;


import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.DoItYourselfStationLogic.Status;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import static com.diy.software.DoItYourselfStationLogic.Status.WAITING_FOR_WEIGHT;

public class MainCustomerPanel extends JPanel {
    Locale ca = new Locale("en", "CA");
    // Create a formatter given the Locale
    NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(ca);
	private JTextField numberOfBags;
    /**
     * Creation of the panel
     * @param bagBarcode 
     */
    public MainCustomerPanel(Customer customer, DoItYourselfStationLogic stationLogic, JTabbedPane tabbedPane, Barcode bagBarcode) {


        setForeground(new Color(128, 128, 255));
        setBackground(SystemColor.inactiveCaption);
        setLayout(null);

        JLabel priceTotal = new JLabel("Cart Total: $0.00");
        priceTotal.setFont(new Font("Georgia", Font.PLAIN, 13));
        priceTotal.setBounds(175, 321, 144, 14);
        add(priceTotal);

        JLabel WeightLabel = new JLabel("Weight: 0.0g");
        WeightLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        WeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        WeightLabel.setBounds(142, 393, 164, 23);
        add(WeightLabel);

        JTextPane scannedItemPane = new JTextPane();
        scannedItemPane.setEditable(false);
        scannedItemPane.setBounds(58, 11, 350, 304);
        add(scannedItemPane);


        JButton scanButton = new JButton("Scan Item");
        scanButton.setBackground(SystemColor.activeCaptionBorder);
        scanButton.setToolTipText("");
        scanButton.setFont(new Font("Georgia", Font.PLAIN, 12));

        // Action event when "Scan" button clicked
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (customer.shoppingCart.isEmpty()){
                    JOptionPane.showMessageDialog(getParent(), "Shopping Cart is Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                    try {
                        scanItem();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(getParent(), "Invalid Item!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                    }
            }

            private void scanItem() {
                //if(stationLogic.scaleController.getStatus() == ScaleController.Status.READY) {
                if (stationLogic.getStatus() == DoItYourselfStationLogic.Status.READY) {
                    customer.selectNextItem();
                    customer.scanItem();
                }
                if (stationLogic.getStatus() != WAITING_FOR_WEIGHT) {
                    customer.deselectCurrentItem();
                    JOptionPane.showMessageDialog(getParent(), "Scan Failed!", "Scan Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // At this point, the status should be "Waiting for Weight"
                JOptionPane.showMessageDialog(getParent(), "Place Item in Bagging!", "Bagging Update", JOptionPane.INFORMATION_MESSAGE);
                bagItem();
                
            }

            private void bagItem() {
				// Customer "puts" the item into the bagging area so now we can compare expected vs actual weight
				customer.placeItemInBaggingArea();
	            updateFields();
				if(stationLogic.getStatus() == DoItYourselfStationLogic.Status.DISCREPANCY) {
					tabbedPane.setSelectedIndex(6);	// Weight Discrepancy
					
					
				}
            }
            private void updateFields() {
            	// Initializing this integer here to avoid duplicate prices
            	int bagTotal = 0;
                WeightLabel.setText("Weight: " + stationLogic.scaleController.getExpectedWeightInGrams() + "g");
                priceTotal.setText("Cart Total: " + (dollarFormat.format(stationLogic.scannerController.getTotal())));

				StringBuilder stringBuilder = new StringBuilder();
				for (BarcodedProduct barcodedProduct : stationLogic.scannerController.getScannedItems()) {
					if(barcodedProduct.getBarcode() != bagBarcode) {
						stringBuilder.append(barcodedProduct.getDescription() + "\t\t\t\t" + dollarFormat.format(barcodedProduct.getPrice()) + "\n");
					}else {
						bagTotal++;
					}
				}
				if(bagTotal != 0) {
					stringBuilder.append(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bagBarcode).getDescription() + "\t\t\t\t" + dollarFormat.format(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bagBarcode).getPrice() * bagTotal) + "\n");
				}
				scannedItemPane.setText(stringBuilder.toString());
            }
        });
        scanButton.setBounds(142, 346, 177, 36);
        add(scanButton);

        // Button for switching to payment tab
        JButton payment = new JButton("Proceed to Payment");
        payment.setBackground(SystemColor.activeCaptionBorder);
        payment.setFont(new Font("Georgia", Font.PLAIN, 13));
        
        // Action event when "Proceed" button clicked
        payment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(2);
            }
        });

        payment.setBounds(142, 481, 177, 36);
        add(payment);
        
        numberOfBags = new JTextField();
        numberOfBags.setBounds(50, 432, 76, 27);
        add(numberOfBags);
        numberOfBags.setColumns(10);

        JLabel bagLabel = new JLabel("Number of Bags");
        bagLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        bagLabel.setBounds(50, 398, 113, 23);
        add(bagLabel);

        JButton purchaseBags = new JButton("Buy Bags");
        purchaseBags.setFont(new Font("Georgia", Font.PLAIN, 13)); 
        
        purchaseBags.addActionListener(new ActionListener() {
        	// Action when the customer wants to buy bags
        	public void actionPerformed(ActionEvent e) {
        		try {
        			int numberOFBags = Integer.parseInt(numberOfBags.getText());
        			for (int i = 0; i < numberOFBags; i++) {
            			stationLogic.scannerController.barcodeScanned(stationLogic.station.scanner, bagBarcode);
            			stationLogic.setStatus(Status.READY);
					}
                    updateBaggingFields();

					
        		}catch (Exception e1) {
					// Catching when the number of bags isn't a valid number
        			JOptionPane.showMessageDialog(getParent(), "Invalid Number of Bags!", "Error", JOptionPane.ERROR_MESSAGE);
				}
        	}

			private void updateBaggingFields() {
				
				int bagTotal = 0;
				WeightLabel.setText("Weight: " + stationLogic.scaleController.getExpectedWeightInGrams() + " grams");
				priceTotal.setText("Cart Total: $" + (stationLogic.scannerController.getTotal()));

				StringBuilder stringBuilder = new StringBuilder();
				for (BarcodedProduct barcodedProduct : stationLogic.scannerController.getScannedItems()) {
					if(barcodedProduct.getBarcode() != bagBarcode) {
						stringBuilder.append(barcodedProduct.getDescription() + "\t\t\t\t" + dollarFormat.format(barcodedProduct.getPrice()) + "\n");
					}else {
						bagTotal++;
					}
				}
				if(bagTotal != 0) {
					stringBuilder.append(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bagBarcode).getDescription() + "\t\t\t" + dollarFormat.format(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(bagBarcode).getPrice() * bagTotal) + "\n");
				}
				scannedItemPane.setText(stringBuilder.toString());
				numberOfBags.setText("");
			}
        });

        purchaseBags.setBounds(166, 427, 95, 35);
        add(purchaseBags);
        
        JButton addOwnBag = new JButton("Use Own Bags");
        addOwnBag.addActionListener(new ActionListener() {
            private void updateFields(BarcodedItem ownBags) {
                double newWeight = stationLogic.scaleController.getExpectedWeightInGrams()+ ownBags.getWeight();
                WeightLabel.setText("Weight: " + newWeight + "g");
                priceTotal.setText("Cart Total: " + (dollarFormat.format(stationLogic.scannerController.getTotal())));
                stationLogic.scaleController.addExpectedWeight(ownBags.getWeight());

                StringBuilder stringBuilder = new StringBuilder();
                for (BarcodedProduct barcodedProduct : stationLogic.scannerController.getScannedItems()) {
                    if(barcodedProduct.getBarcode() != bagBarcode) {
                        stringBuilder.append(barcodedProduct.getDescription() + "\t\t\t\t" + dollarFormat.format(barcodedProduct.getPrice()) + "\n");
                    }
                }
                scannedItemPane.setText(stringBuilder.toString());
            }
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(getParent(), "Please add your bag!", "Bagging Update", JOptionPane.INFORMATION_MESSAGE);
        		// At this point the attendant should be pinged
        		tabbedPane.setSelectedIndex(1);
                Barcode barcode = new Barcode(new Numeral[]{Numeral.valueOf((byte) 7)});
                BarcodedItem ownBags = new BarcodedItem(barcode, 10);
        		// Notifying using the scale that the weight has changed
        		updateFields(ownBags);
            }
        });
        addOwnBag.setFont(new Font("Georgia", Font.PLAIN, 13));
        addOwnBag.setBounds(283, 427, 125, 35);
        add(addOwnBag);
    }
}