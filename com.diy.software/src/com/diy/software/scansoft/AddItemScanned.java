package com.diy.software.scansoft;


import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import com.jimmyselectronics.necchi.*;
import com.diy.hardware.*;

import ca.powerutility.NoPowerException;
import ca.ucalgary.seng300.simulation.*;



public class AddItemScanned{
	

	private ArrayList<BarcodedItem> addedItems = new ArrayList<BarcodedItem>();
	
	
	// make a list of the added item prices so its easier to remove
	//private ArrayList<Double> addedItemsPrice = new ArrayList<Double>();
	
	
	//make a list of added item weights? and expected weights ? so thats its easier to remove? Maybe for 2nd it..?
	
	private Map<Barcode,BarcodedProduct> availableProducts = new HashMap<>();
	
	private double total = 0;
	private double expectedWeight = 0;
	//private BarcodedItem item = null;
	private BarcodeScanner scanner = null;
	
	
	
	/*
	 * Basic Constructor
	 * 
	 * @param ap
	 * 		Available products at the store
	 * 
	 * @param s
	 * 		The scanner that will be used
	 * 
	 */
	public AddItemScanned(Map<Barcode,BarcodedProduct> ap, BarcodeScanner s) throws NoPowerException {
		availableProducts = ap;
		scanner = s;
		 
	}
	
	
	public boolean add(BarcodedItem item) throws InvalidArgumentSimulationException {
		if (scanner.scan(item)) {
			if (availableProducts.containsKey(item.getBarcode())) {
				addedItems.add(item);
				//expected weight of bagging area
				expectedWeight += availableProducts.get(item.getBarcode()).getExpectedWeight();
				//checks if the product price is by weight or by unit
				boolean perUnit= availableProducts.get(item.getBarcode()).isPerUnit();
				
				double price;
				
				
				if (perUnit) {
					price = availableProducts.get(item.getBarcode()).getPrice();
					total += price; }
				else {
					//price is by weight so item weight * price
					price = item.getWeight()*availableProducts.get(item.getBarcode()).getPrice();
					total += price;
				}
				
				return true;
			}
			else return false;
		}
		return false;

	}

	public void addOwnBagToExpectedWeight(BarcodedItem ownBag) {
		expectedWeight += ownBag.getWeight();
	}
	
	public double getTotal() {
		return total;
	}
	
	// update expected weight
	public double getExpectedWeight() {
		return expectedWeight;
	}
	
		//this method was added for testing purposes. Need to return boolean for if cart empty
	public boolean isAddedItemsempty() {
		if (addedItems.isEmpty() == true) {
			return true;
		} else {
			return false;
		}
	}
	

	 
	
	 
	 //item has barcode -> (barcode) Barcodedproduct -> product which has price
	 //product database? Assume its set up
	 // Java hashmap..
	 //function that both ads item to a list and returns cost of item
	 //get the price 
	 

}
