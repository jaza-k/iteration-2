package com.diy.software.test.iteration1;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.diy.hardware.BarcodedProduct;
import com.diy.software.scansoft.AddItemScanned;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;

public class AddItemScannedTest {
	Map<Barcode,BarcodedProduct> availableProducts = new HashMap<>();
	Barcode [] barcodes = new Barcode[10];
	
	BarcodeScanner scanner = new BarcodeScanner();
	Barcode barcode = new Barcode(new Numeral[] {Numeral.valueOf((byte)1)});
	Barcode barcode2 = new Barcode(new Numeral[] {Numeral.valueOf((byte)1)});
	BarcodedItem item = new BarcodedItem(barcode,10);
	BarcodedItem item2 = new BarcodedItem(barcode2,10);
	AddItemScanned test = new AddItemScanned(availableProducts,scanner);

	
	@Before
	public void setUp() throws Exception {
		scanner.plugIn();
		scanner.turnOn();
		
		
		//review this code
		for (int i = 0; i < 10; i++) {
			barcodes[i] = new Barcode(new Numeral[] { Numeral.valueOf((byte)i)});
			BarcodedProduct product = new BarcodedProduct(barcodes[i], "prod"+i, (long)(1.0+i),5);
			availableProducts.put(barcodes[i], product);
			
		}
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void addItemTest() { //adding item with barcode
		assertTrue(test.isAddedItemsempty());
		test.add(item);
		assertFalse(test.isAddedItemsempty());	
	}
	

	@SuppressWarnings("deprecation")
	@Test
	public void ItemweightTest() { //adding item without barcode
		assertTrue(test.isAddedItemsempty());
		test.add(item);
		assertEquals(5.0, test.getExpectedWeight(), 0);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void ItemCostTest() { //adding item without barcode
		assertTrue(test.isAddedItemsempty());
		test.add(item);
		assertEquals(2.0, test.getTotal(), 0);
	}
	
	@Test
	public void failedScan() { //adding item without barcode
		boolean flag = true;
		while (flag == true)
		{
			if (test.add(item) == true) {
			} else {
				flag = false;
			}
		}
		assertFalse(flag);
	}
}