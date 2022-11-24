package com.diy.software.gui;

import com.diy.software.DoItYourselfStationLogic;

public class Cart {
	private double total;
	private DoItYourselfStationLogic stationLogic;
	
	public Cart( DoItYourselfStationLogic stationLogic) {
		this.total = 0;
		this.stationLogic = stationLogic;
		
	}
	
	public void updateTotal() {
		this.total = stationLogic.scannerController.getTotal();
	}
	
	public double getTotal() {
		return total;
	}
	
}
