package com.aoi.laundryapi

import org.scalatra._
import scalate.ScalateSupport
import org.json4s._
import org.json4s.jackson.JsonMethods._

class MyScalatraServlet extends LaundryapiStack {
  var ls = new LaundryScraper();
  ls.scrape();
  
  print(parse(LaundryScraper.halls));
      
  get("/") {
    <html>
      <body>
        <h1>Laundry API for UNC Charlotte</h1>
        
      </body>
    </html>
  }
  get("/all") {
    
  }
}
