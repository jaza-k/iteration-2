package com.diy.software.scansoft;

import java.util.HashMap;
import java.util.Map;

import com.diy.hardware.BarcodedProduct;

import com.jimmyselectronics.necchi.*;

public class Addtest {
	
	
	
	

	
	
	public static void main(String[] args) {
		
		BarcodeScanner scanner = new BarcodeScanner();
		scanner.plugIn();
		scanner.turnOn();
		Map<Barcode,BarcodedProduct> availableProducts = new HashMap<>();
		Barcode [] barcodes = new Barcode[10];
		
		
		//System.out.println("THIS IS A TEST THIS IS A TEST THIS IS A TEST THIS IS A TEST");
		
		for (int i = 0; i < 10; i++) {
			barcodes[i] = new Barcode(new Numeral[] { Numeral.valueOf((byte)i)});
			BarcodedProduct product = new BarcodedProduct(barcodes[i], "prod"+i, (long)(1.0+i),5);
			availableProducts.put(barcodes[i], product);
			
		}
		
		Barcode barcode = new Barcode(new Numeral[] {Numeral.valueOf((byte)1)});
		BarcodedItem item = new BarcodedItem(barcode,10);
		
		AddItemScanned test = new AddItemScanned(availableProducts,scanner);
		
		test.add(item);
		
		System.out.println(test.getTotal());
	
		Barcode barcode2 = new Barcode(new Numeral[] {Numeral.valueOf((byte)2)});
		BarcodedItem item2 = new BarcodedItem(barcode2, 10);
		test.add(item2);
		
		System.out.println(test.getTotal());
		
		
	}

}
