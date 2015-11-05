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
    convertToJson(halls)
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
  get("/api/hall/:hallName/dryers/?"){
    convertToJson(find.getDryers({params("hallName")}, {params("floor")}));
  }
  get("/api/hall/:hallName/washers/?"){
    convertToJson(find.getWashers({params("hallName")}, {params("floor")}));
  }
  get("/api/hall/:hallName/:floor/dryers/?"){
    convertToJson(find.getDryers({params("hallName")}, {params("floor")}));
  }
  get("/api/hall/:hallName/:floor/washers/?"){
    convertToJson(find.getWashers({params("hallName")}, {params("floor")}));
  }
  get("/api/hall/:hallName/:floor/dryers/:dryerName"){
    convertToJson(find.getDryerStatus({params("hallName")}, {params("floor")}, {params("dryerName")}));
  }
  get("/api/hall/:hallName/:floor/washers/:washerName"){
    convertToJson(find.getDryerStatus({params("hallName")}, {params("floor")}, {params("washerName")}));
  }
  def convertToJson[T](obj : T) : String = {
    var obm = new ObjectMapper();
    obm.configure(SerializationFeature.INDENT_OUTPUT, true);
    var stringObject = new StringWriter();
    obm.writeValue(stringObject, obj);
    return stringObject.toString();
  }
}
