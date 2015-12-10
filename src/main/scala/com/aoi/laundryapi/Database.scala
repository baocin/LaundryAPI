package com.aoi.laundryapi

import java.sql.{Connection, DriverManager, ResultSet,SQLException, PreparedStatement};
import java.util.ArrayList
import scala.collection.JavaConversions._
import scala.collection.immutable.HashMap

object Database{
  val url = "jdbc:mysql://localhost/Laundry"
  val username = "root"
  val password = "j"
  var connection:Connection = null


  try {
    connection = DriverManager.getConnection("jdbc:mysql://localhost/Laundry?" +
                                 "user=root&password=j")
  }catch {
    case e : Throwable => e.printStackTrace
  }


  def insert(inHalls : ArrayList[Hall]) {
    var find = new LaundrySorter(inHalls)
    val statement = connection.createStatement()

    for ( hall <- inHalls){
      var numWashers = find.getWashers(hall.getHallName, hall.getFloorNumber.toString).length;
      var numDryers = find.getDryers(hall.getHallName, hall.getFloorNumber.toString).length;

      //Get the id of the room we just made (cause it is unique)
      val getRoomIDQuery = s"""SELECT room_id from rooms WHERE url="${hall.getUrl}""""
      var resultRoomSearch = statement.executeQuery(getRoomIDQuery)

      //if false then its an empty set
      var firstRow = resultRoomSearch.first();
      println()
      var hallRowID =  0;

      if (firstRow){ //update old
        hallRowID = resultRoomSearch.getInt("room_id");

        println(s"updating old room ${hall.getHallName} ${hall.getFloorNumber}")
        val roomUpdateQuery = s"""UPDATE rooms SET washer_count=${numWashers}, dryer_count=${numDryers} WHERE url="${hall.getUrl}""""
        var preparedStmt : PreparedStatement = connection.prepareStatement(roomUpdateQuery)
        preparedStmt.execute()
      }else{    //make new
        println(s"making new room ${hall.getHallName} ${hall.getFloorNumber}")
        val roomInsertQuery = "INSERT INTO rooms (floor_number, hall_name, url, washer_count,dryer_count) VALUES (?,?,?,?,?)"
        var preparedStmt : PreparedStatement = connection.prepareStatement(roomInsertQuery)
        preparedStmt.setInt(1, hall.getFloorNumber)
        preparedStmt.setString(2, hall.getHallName)
        preparedStmt.setString(3, hall.getUrl)
        preparedStmt.setInt(4, numWashers)
        preparedStmt.setInt(5, numDryers)
        preparedStmt.execute()

        var rs = statement.executeQuery("select last_insert_id() as last_id from rooms");
        rs.first()
        hallRowID = rs.getString("last_id").toInt;
      }


      //
      // val washerInsertQuery = "INSERT INTO washers (name) VALUES (?)"
      // for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
      //   var preparedStmt : PreparedStatement = connection.prepareStatement(washerInsertQuery)
      //   preparedStmt.setString(1, washer.getName)
      //   preparedStmt.execute()
      //   var rs = statement.executeQuery("select last_insert_id() as last_id from washers");
      //   rs.first()
      //   var lastid = rs.getString("last_id");
      //   washerIDList.append(lastid.toInt)
      // }

      // val washerRoomMapInsertQuery = "INSERT INTO washer_room_map (washer_id, room_id, time_left, raw_status) VALUES (?, ?, ?, ?)"
      // for (washerID <- washerIDList; washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
      //   var preparedStmt : PreparedStatement = connection.prepareStatement(washerRoomMapInsertQuery)
      //   preparedStmt.setInt(1, washerID)
      //   preparedStmt.setInt(2, hallRowID)
      //   preparedStmt.setInt(3, washer.getNumMinutesLeft)
      //   preparedStmt.setString(4, washer.getRawStatus)
      //   preparedStmt.execute()
      // }
      //
      // //insert dryers
      // var dryerIDList = new HashMap[Int, String]();
      // val dryerInsertQuery = "INSERT INTO dryers (name) SELECT ? WHERE NOT EXISTS (SELECT name FROM dryers WHERE name=@?)"
      // for (dryer <- find.getDryers(hall.getHallName, hall.getFloorNumber.toString)){
      //   var preparedStmt : PreparedStatement = connection.prepareStatement(dryerInsertQuery)
      //   preparedStmt.setString(1, dryer.getName)
      //   preparedStmt.setString(2, dryer.getName)
      //   preparedStmt.execute()
      //   var rs = statement.executeQuery("select last_insert_id() as last_id from dryers");
      //   rs.first()
      //   var lastid = rs.getString("last_id");
      //   dryerIDList += (lastid.toInt -> dryer.getName)
      //   println("Dryer List: " + dryerIDList)
      // }
      //
      // //insert washers
      // var washerIDList = new HashMap[Int, String]();
      // val washerInsertQuery = "INSERT INTO washers (name) SELECT ? WHERE NOT EXISTS (SELECT name FROM washers WHERE name=@?)"
      // for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
      //   var preparedStmt : PreparedStatement = connection.prepareStatement(washerInsertQuery)
      //   preparedStmt.setString(1, washer.getName)
      //   preparedStmt.setString(2, washer.getName)
      //   preparedStmt.execute()
      //   var rs = statement.executeQuery("select last_insert_id() as last_id from washers");
      //   rs.first()
      //   var lastid = rs.getString("last_id");
      //   washerIDList += (lastid.toInt -> washer.getName)
      //   println("Washer List: " + washerIDList)
      // }


      val dryerRoomMapInsertQuery = "INSERT INTO dryer_room_map (dryer_name, room_id, time_left, raw_status) VALUES (?, ?, ?, ?)"
      for (dryer <- find.getDryers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(dryerRoomMapInsertQuery)
        preparedStmt.setString(1, dryer.getName)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.setInt(3, dryer.getNumMinutesLeft)
        preparedStmt.setString(4, dryer.getRawStatus)
        preparedStmt.execute()
      }

      val washerRoomMapInsertQuery = "INSERT INTO washer_room_map (washer_name, room_id, time_left, raw_status) VALUES (?, ?, ?, ?)"
      for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(washerRoomMapInsertQuery)
        preparedStmt.setString(1, washer.getName)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.setInt(3, washer.getNumMinutesLeft)
        preparedStmt.setString(4, washer.getRawStatus)
        preparedStmt.execute()
      }
    }
  }
  //
  // def getToday() : String {
  //   val statement = connection.createStatement()
  //   var rs = statement.executeQuery("SELECT room_id, floor_number, washer_id, time_left, raw_status FROM rooms JOIN washer_room_map ON rooms.room_id = washer_room_map.room_id JOIN washers ON washers.washer_id = washer_room_map.washer_id");
  //
  // }


}
