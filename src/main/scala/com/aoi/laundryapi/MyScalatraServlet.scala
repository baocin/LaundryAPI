package com.aoi.laundryapi

import org.scalatra._
import scalate.ScalateSupport
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.util.ArrayList
import com.fasterxml.jackson._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.StringWriter

class MyScalatraServlet extends LaundryapiStack {
  var ls = new LaundryScraper();
  var halls = ls.scrape();
  
  var find : LaundrySorter = new LaundrySorter(halls);
  
  
  
  get("/") {
    <html>
      <body>
        <h1>Laundry API for UNC Charlotte</h1>
        This api allows anyone to get up to date information about any laundry room on the UNC Charlotte campus!
      </body>
    </html>
  }
  get("/api/all") {
    convertToJson(halls)
  }
  get("/api/hall/:hallName/?"){
    convertToJson(find.getHalls({params("hallName")}))
  }
  get("/api/hall/:hallName/:floor"){
    convertToJson(find.getFloor({params("hallName")}, {params("floor")}))
  }
  def convertToJson[T](obj : T) : String = {
    var obm = new ObjectMapper();
    obm.configure(SerializationFeature.INDENT_OUTPUT, true);
    var stringObject = new StringWriter();
    obm.writeValue(stringObject, obj);
    return stringObject.toString();
  }
}
