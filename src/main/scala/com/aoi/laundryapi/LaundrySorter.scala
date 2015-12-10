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
        //print(hall);
        return hall;
      }
    }
    return null;
  }
  def getDryerStatus (hallName : String, floorNum : String, machineName : String) : LaundryMachine = {
    var floor = getFloor(hallName, floorNum);
    var it = floor.getDryers.iterator();
    while (it.hasNext()){
      var machine = it.next();
      println(machine.getName + " == " + machineName + "   " + (machine.getName == machineName))
      if (machine.getName == machineName){
        return machine;
      }
    }
    return null;
  }
  def getWasherStatus (hallName : String, floorNum : String, machineName : String) : LaundryMachine = {
    var floor = getFloor(hallName, floorNum);
    var it = floor.getWashingMachines.iterator();
    while (it.hasNext()){
      var machine = it.next();
      if (machine.getName == machineName){
        return machine;
      }
    }
    return null;
  }

  def getDryers (hallName : String, floorNum : String) : ArrayList[Dryer] = {
    var floor = getFloor(hallName, floorNum);
    return floor.getDryers;
  }
  def getWashers (hallName : String, floorNum : String) : ArrayList[WashingMachine] = {
    var floor = getFloor(hallName, floorNum);
    return floor.getWashingMachines;
  }

  // def groupByHall () : String{
  //   val it = halls.iterator();
  //   var groupedHalls = new HashMap[String, Hashmap[String, ]]
  //   // var newFormat = {"rooms" -> [Hall], "totalWashingMachinesAvailable" -> 0, "totalWashingMachines" -> 0}
  //   // var groupedHalls = new ArrayList[newFormat];
  //
  //   for (hall <- halls)
  //     println(hall)
  //     // if (hall.getHallName.toLowerCase().contains(name.toLowerCase())){
  //     //   matchingHalls.add(hall)
  //     // }
  //   }
  //   newJson
  // }
//  def getMachineStatus (hallName : String, floorNum : String, machineName : String) : LaundryMachine = {
//    var floor = getFloor(hallName, floorNum);
//    var it = floor.getWashingMachines
//    for (Hall h : floor){
//      print(h);
//    }
//  }
//  def machineStatus(name : String, floorNum
}
