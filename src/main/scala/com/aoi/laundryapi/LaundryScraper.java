package com.aoi.laundryapi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
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
		ArrayList<Hall> tempHalls = new ArrayList<Hall>();		//store the new scrape in temporary arraylist so when switching the data is always whole
		Elements hallLinks = doc.select("#rooms li a");
		Pattern floorNumRegex = Pattern.compile("([0-9])");

		for (Element hallLink : hallLinks){
			String rawName = hallLink.text();
			String [] splitName = rawName.split(" - ");
			String hallName = splitName[0].trim().replaceAll(" HALL", "");

			int floorNumber = -1;
			if (splitName.length >= 2){
				Scanner inName = new Scanner(rawName);
				Pattern locationPattern = Pattern.compile("([0-9])");
				Matcher matches = locationPattern.matcher(rawName);

				if (matches.find() && matches.groupCount() >= 1){
					floorNumber = Integer.parseInt(matches.group(1));
				}
			}

			String url = hallLink.attr("href");
			Long id = Long.parseLong(hallLink.attr("id"));

			tempHalls.add(new Hall(
				floorNumber,
				hallName,
				id,
				baseURL + url
			));
		}

		halls = scrapeHalls(tempHalls);
		return halls;
	}

	public ArrayList<Hall> scrapeHalls(ArrayList<Hall> hallList){
		for (Hall hall: hallList){
//			System.out.println("Getting " + hall.getHallName() + "(" +  hall.getFloorNumber() + ")");
			//http://m.laundryview.com/submitFunctions.php?monitor=true&lr=3268853&cell=null&_=1441953680236
			String hallURL = baseURL + "submitFunctions.php?monitor=true&lr=" + hall.getId();
			hall.setUrl(hallURL);
			hall.scrapeHallStatus();
			hall.scrapeDryers();
			hall.scrapeWashingMachines();
			System.out.print(hall.getHallName().split(" ")[0] + " ");
		}
		System.out.println();
		return hallList;
//		System.out.println(halls);

	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (Hall hall : halls){
			sb.append(hall.toString());
		}
		return sb.toString();
	}

}
