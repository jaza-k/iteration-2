package com.diy.software.payment;

public class Member{
	private String memNum;
	private String nameOf;
	
	public void setNumb (String numb) {
		memNum = numb;
	}
	
	public void setName (String name) {
		nameOf = name;
	}
	
	public String getNumb () {
		return memNum;
	}
	
	public String getName () {
		return nameOf;
	}
	
}
