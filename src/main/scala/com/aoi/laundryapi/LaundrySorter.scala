package com.aoi.laundryapi

import java.util.ArrayList

class LaundrySorter {
  var halls : ArrayList[Hall] = null;
  
  def this(inHalls : ArrayList[Hall]) {
    this();
    this.halls = inHalls;
  }
  
  def getHalls(name : String) : ArrayList[Hall] = {
    val it = halls.iterator();
    var matchingHalls = new ArrayList[Hall];
    while (it.hasNext()){
      var hall = it.next();
      if (hall.getHallName.toLowerCase().contains(name.toLowerCase())){
        matchingHalls.add(hall)
      }
    }
    return matchingHalls;
  }
  
  def getFloor(name : String, floorNum : String) : Hall = {
    var floorNumber = Integer.parseInt(floorNum)
    var floors = getHalls(name);
    val it = floors.iterator()
    while(it.hasNext()){
      var hall = it.next()
      if (hall.getFloorNumber == floorNumber){
        print(hall);
        return hall;
      }
    }
    return null;
  }
//  def machineStatus(name : String, floorNum
}