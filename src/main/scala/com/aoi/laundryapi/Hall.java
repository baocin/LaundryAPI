package com.aoi.laundryapi;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Hall extends Utils{
	private int floorNumber;
	private String hallName;
	private long id;
	private String url;
	private ArrayList<WashingMachine> washingMachines = new ArrayList<WashingMachine>();
	private ArrayList<Dryer> dryers = new ArrayList<Dryer>();
	private int availableNumWashingMachines;
	private int totalNumWashingMachines;
	private int totalNumDryers;
	private int availableNumDryers;

	public Hall(){
		floorNumber = -1;
		hallName = "";
		id = -1;
		url = "";
		washingMachines = new ArrayList<WashingMachine>();
		dryers = new ArrayList<Dryer>();

	}

	@Override
	public String toString() {
		return "Hall [floorNumber=" + floorNumber + ", hallName=" + hallName + ", id=" + id + ", url=" + url
				+ ", washingMachines=" + washingMachines + ", dryers=" + dryers + "]";
	}


	public ArrayList<WashingMachine> getWashingMachines() {
		return washingMachines;
	}


	public ArrayList<Dryer> getDryers() {
		return dryers;
	}


	public void setWashingMachines(ArrayList<WashingMachine> washingMachines) {
		this.washingMachines = washingMachines;
	}


	public void setDryers(ArrayList<Dryer> dryers) {
		this.dryers = dryers;
	}


	public Hall(int floorNum, String name, long id, String url){
		this.floorNumber = floorNum;
		this.hallName = name;
		this.id = id;
		this.url = url;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public String getHallName() {
		return hallName;
	}

	public long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void scrapeHallStatus(){
		Document doc = download(url);
		Elements dryers = doc.select("#dryer span");
		Elements washingMachines = doc.select("#washer span");

		Pattern availablePattern  = Pattern.compile("([0-9]+)\\s*of\\s*([0-9]+)");
		Matcher matchedAvailability = availablePattern.matcher(dryers.text());
		matchedAvailability.find();
		if (matchedAvailability.groupCount() >= 2){
			int currentAvailable = Integer.parseInt(matchedAvailability.group(1));
			int total = Integer.parseInt(matchedAvailability.group(2));
			totalNumDryers = total;
			availableNumDryers = currentAvailable;
		}

		matchedAvailability = availablePattern.matcher(washingMachines.text());
		matchedAvailability.find();
		if (matchedAvailability.groupCount() >= 2){
			int currentAvailable = Integer.parseInt(matchedAvailability.group(1));
			int total = Integer.parseInt(matchedAvailability.group(2));
//			System.out.println(currentAvailable + " / " + total);
			totalNumWashingMachines = total;
			availableNumWashingMachines = currentAvailable;
		}
	}

	public void scrapeWashingMachines(){
		Document doc = download(url);
		Elements rawWashingMachineList = doc.select("li:has(img[src*=\"washer\"])");

		for (Element dryer : rawWashingMachineList){
			WashingMachine wm = new WashingMachine();
			String [] statusSplit = dryer.select("a").text().split("\\s");

			if (statusSplit.length >= 2){
				String name = statusSplit[0];
				String status = statusSplit[1].toLowerCase();

				wm.setName(name);
				wm.setRawStatus(status);
				wm.setAvailable(false);

				if (status.contains("avail")){
					wm.setAvailable(true);
					wm.setNumMinutesLeft(-1);
				}else{
					try{
						wm.setNumMinutesLeft(Integer.parseInt(status));
					}catch(NumberFormatException e){

					}
					wm.setAvailable(false);

				}
			}

			washingMachines.add(wm);
		}

	}

	public void scrapeDryers(){
		Document doc = download(url);
		Elements rawDryerList = doc.select("li:has(img[src*=\"dryer\"])");

		for (Element dryer : rawDryerList){
			Dryer dr = new Dryer();
			String [] statusSplit = dryer.select("a").text().split("\\s");

			if (statusSplit.length >= 2){
				String name = statusSplit[0];
				String status = statusSplit[1].toLowerCase();

				dr.setName(name);
				dr.setRawStatus(status);
				dr.setAvailable(false);

				if (status.contains("avail")){
					dr.setAvailable(true);
					dr.setNumMinutesLeft(-1);
				}else{
					try{
						dr.setNumMinutesLeft(Integer.parseInt(status));
					}catch(NumberFormatException e){

					}
					dr.setAvailable(false);

				}
			}

			dryers.add(dr);
		}

	}


}
