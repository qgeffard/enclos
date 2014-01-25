package com.enclos.data;

public enum BridgeRotation {
	DOWNWARD(30),
	UPWARD(300);
	
	private int radian = 0;
	
	private BridgeRotation(int rad){
		this.radian = rad;
	}
	
	public int getRotation(){
		return this.radian;
	}
}
