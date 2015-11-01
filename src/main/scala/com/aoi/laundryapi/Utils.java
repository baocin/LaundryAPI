package com.aoi.laundryapi;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
	public static Document download(String url){
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.userAgent("Mozilla")
					.timeout(3000)
					.post();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
}
