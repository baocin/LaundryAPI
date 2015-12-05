package com.aoi.laundryapi;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
	public static Document download(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.userAgent("Googlebot/2.1 (+http://www.google.com/bot.html)")
					.timeout(60*1000)		//10s
					.post();
	  } catch (java.net.SocketTimeoutException e){
			//Log!
			// System.out.println("Timeout Error!!");

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
}
