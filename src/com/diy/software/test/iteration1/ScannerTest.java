package com.diy.software.test.iteration1;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.diy.software.scanner.InvalidItemException;
import com.diy.software.scanner.ScanFailureException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.diy.hardware.BarcodedProduct;
import com.diy.software.controllers.ScannerController;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;

public class ScannerTest {
    Map<Barcode, BarcodedProduct> availableProducts = new HashMap<>();
    Barcode[] barcodes = new Barcode[10];

    BarcodeScanner scanner = new BarcodeScanner();
    Barcode barcode = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem item = new BarcodedItem(barcode, 10);
    ScannerController test = new ScannerController(availableProducts, scanner);

    @Before
    public void setUp() {
        scanner.plugIn();
        scanner.turnOn();

        // review this code
        for (int i = 0; i < 10; i++) {
            barcodes[i] = new Barcode(new Numeral[]{Numeral.valueOf((byte) i)});
            BarcodedProduct product = new BarcodedProduct(barcodes[i], "prod" + i, (long) (1.0 + i), 5);
            availableProducts.put(barcodes[i], product);
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void AddItemTest() throws ScanFailureException, InvalidItemException { //adding item with barcode
        assertTrue(test.getScannedItems().isEmpty());
        test.Scan(item);
        assertFalse(test.getScannedItems().isEmpty());
    }


    @Test
    public void ItemWeightTest() throws ScanFailureException, InvalidItemException { //adding item without barcode
        assertTrue(test.getScannedItems().isEmpty());
        test.Scan(item);
        assertEquals(5.0, test.getExpectedWeight(), 0);
    }

    @Test
    public void ItemCostTest() throws ScanFailureException, InvalidItemException { //adding item without barcode
        assertTrue(test.getScannedItems().isEmpty());
        test.Scan(item);
        assertEquals(2.0, test.getTotal(), 0);
    }

    @Test
    public void failedScan() throws InvalidItemException { // scanning error
        while (true) {
            try {
                test.Scan(item);
            } catch (ScanFailureException e) {
                return;
            }
        }
    }
}