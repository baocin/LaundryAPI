package com.aoi.laundryapi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LaundryScraper extends Utils {
	public ArrayList<Hall> halls = new ArrayList<Hall>();
	public String baseURL = "http://m.laundryview.com/";

	public ArrayList<Hall> scrape() {
		Document doc = Utils.download("http://m.laundryview.com/uncc");

		Elements hallLinks = doc.select("#rooms li a");
		System.out.println("Found " + hallLinks.size() + " hall Links");
		Pattern floorNumRegex = Pattern.compile("([0-9])");

		for (Element hallLink : hallLinks){
			String rawName = hallLink.text();
			String [] splitName = rawName.split(" - ");
			System.out.println("Processing " + rawName);
			String hallName = splitName[0];

			int floorNumber = -1;
			if (splitName.length >= 2){
				floorNumber = (int)(splitName[1].charAt(0)) - 48;
			}

			String url = hallLink.attr("href");
			Long id = Long.parseLong(hallLink.attr("id"));

			halls.add(new Hall(
				floorNumber,
				hallName,
				id,
				baseURL + url
			));
		}

		scrapeHalls();
		return halls;
	}

	public void scrapeHalls(){
		for (Hall hall: halls){
			System.out.println("Getting " + hall.getHallName() + "(" +  hall.getFloorNumber() + ")");
			//http://m.laundryview.com/submitFunctions.php?monitor=true&lr=3268853&cell=null&_=1441953680236
			String hallURL = baseURL + "submitFunctions.php?monitor=true&lr=" + hall.getId();
			hall.setUrl(hallURL);
			hall.scrapeHallStatus();
			hall.scrapeDryers();
			hall.scrapeWashingMachines();
		}
		System.out.println(halls);

	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (Hall hall : halls){
			sb.append(hall.toString());
		}
		return sb.toString();
	}

}
