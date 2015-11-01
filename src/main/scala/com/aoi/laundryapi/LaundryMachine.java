package com.aoi.laundryapi;

public class LaundryMachine {

	private String name;
	private int numMinutesLeft;
	private boolean available;
	private String rawStatus;

	public String getRawStatus() {
		return rawStatus;
	}
	public void setRawStatus(String status) {
		this.rawStatus = status;
	}

	public LaundryMachine(){
		this.name = "";
		this.numMinutesLeft = -1;
		this.available = false;
	}
	public LaundryMachine(String name, int numMinutesLeft, boolean available, boolean dryer, boolean washer) {
		super();
		this.name = name;
		this.numMinutesLeft = numMinutesLeft;
		this.available = available;
	}
	public String getName() {
		return name;
	}
	public int getNumMinutesLeft() {
		return numMinutesLeft;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNumMinutesLeft(int numMinutesLeft) {
		this.numMinutesLeft = numMinutesLeft;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	@Override
	public String toString() {
		return "LaundryMachine [name=" + name + ", numMinutesLeft=" + numMinutesLeft + ", available=" + available
				+ ", status=" + rawStatus + "]";
	}


}
