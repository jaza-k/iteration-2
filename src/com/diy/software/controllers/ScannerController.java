package com.diy.software.controllers;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.diy.hardware.external.ProductDatabases;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.scanner.InvalidItemException;
import com.diy.software.scanner.ScanFailureException;
import com.jimmyselectronics.necchi.*;
import com.diy.hardware.*;
import com.jimmyselectronics.virgilio.ElectronicScale;
import com.jimmyselectronics.virgilio.ElectronicScaleListener;

public class ScannerController implements BarcodeScannerListener {


    private ArrayList<BarcodedProduct> scannedItems = new ArrayList();
    private Map<Barcode, BarcodedProduct> availableProducts;
    private double total = 0;
    private double expectedWeight = 0;
    private DoItYourselfStationLogic diyStationLogic;

    /**
     * Basic Constructor
     *
     * @param availableProducts All products available at the store
     * @param scanner           The scanner that will be used
     */
    public ScannerController(DoItYourselfStationLogic diyStationLogic) {
        this.diyStationLogic = diyStationLogic;
    }

    /**
     * Scans an item
     * Increases the total cost and weight
     *
     * @param item The item being scanned
     * @throws ScanFailureException if unable to scan the item
     * @throws InvalidItemException if the barcode has not been registered
     */
    public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
        // Ignore when there is no product associated with the barcode
        if(!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode))
            return;

        var product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

        scannedItems.add(product);
        expectedWeight += availableProducts.get(item.getBarcode()).getExpectedWeight();
        boolean perUnit = availableProducts.get(item.getBarcode()).isPerUnit();
        double price;
        if (perUnit)
            price = availableProducts.get(item.getBarcode()).getPrice();
        else
            price = item.getWeight() * availableProducts.get(item.getBarcode()).getPrice();
        total += price;
    }

    /**
     * Obtains the total value of the items scanned with this machine
     *
     * @return The total value of items scanned during the current transaction.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Obtains the total expected weight of the items scanned with this machine
     *
     * @return The total expected weight of items scanned during the current transaction.
     */
    public double getExpectedWeight() {
        return expectedWeight;
    }

    /**
     * Obtains the list of items scanned with this machine
     *
     * @return The total list of items scanned during the current transaction.
     */
    public List<BarcodedItem> getScannedItems() {
        return scannedItems;
    }

    /**
     * Clears the current list of items scanned with this machine and reset the total cost and weight
     */
    public void clearScanned() {
        scannedItems.clear();
        total = 0;
        expectedWeight = 0;
    }
}
