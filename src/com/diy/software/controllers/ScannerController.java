package com.diy.software.controllers;


import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodeScannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScannerController implements BarcodeScannerListener {


    private ArrayList<BarcodedProduct> scannedItems = new ArrayList();
    private double total = 0;
    private DoItYourselfStationLogic stationLogic;

    /**
     * Basic Constructor
     *
     * @param stationLogic Logic instance that is using this controller
     */
    public ScannerController(DoItYourselfStationLogic stationLogic) {
        this.stationLogic = stationLogic;
    }

    /**
     * Barcode scanned listener
     *
     * @param barcodeScanner The barcode scanner used to scan the item
     * @param barcode        The barcode of the scanned item
     */
    public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
        // Ignore when there is no product associated with the barcode
        if (!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode))
            return;

        var product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

        scannedItems.add(product);
        boolean perUnit = product.isPerUnit();
        double price;
        if (perUnit)
            price = product.getPrice();
        else
            price = product.getExpectedWeight() * product.getPrice();
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
     * Obtains the list of items scanned with this machine
     *
     * @return The total list of items scanned during the current transaction.
     */
    public List<BarcodedProduct> getScannedItems() {
        return scannedItems;
    }

    /**
     * Clears the current list of items scanned with this machine and reset the total cost
     */
    public void reset() {
        scannedItems.clear();
        total = 0;
    }
}
