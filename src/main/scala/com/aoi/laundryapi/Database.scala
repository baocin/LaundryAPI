package com.aoi.laundryapi

import java.sql.{Connection, DriverManager, ResultSet,SQLException, PreparedStatement};
import java.util.ArrayList
import scala.collection.JavaConversions._

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
        val roomUpdateQuery = s"""UPDATE rooms SET num_available_washers=${numAvailableWashers}, washer_count=${numWashers}, dryer_count=${numDryers} WHERE url="${hall.getUrl}""""
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

      val washerInsertQuery = "INSERT INTO washers (name) VALUES (?)"
      var washerIDList = new ArrayList[Int]()
      for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(washerInsertQuery)
        preparedStmt.setString(1, washer.getName)
        preparedStmt.execute()
        var rs = statement.executeQuery("select last_insert_id() as last_id from washers");
        rs.first()
        var lastid = rs.getString("last_id");
        washerIDList.append(lastid.toInt)
      }

      var dryerIDList = new ArrayList[Int]()
      val dryerInsertQuery = "INSERT INTO dryers (name) VALUES (?)"
      for (dryer <- find.getDryers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(dryerInsertQuery)
        preparedStmt.setString(1, dryer.getName)
        preparedStmt.execute()

        var rs = statement.executeQuery("select last_insert_id() as last_id from dryers");
        rs.first()
        var lastid = rs.getString("last_id");
        dryerIDList.append(lastid.toInt)
      }

      val washerRoomMapInsertQuery = "INSERT INTO washer_room_map (washer_id, room_id, time_left, raw_status) VALUES (?, ?, ?, ?)"
      for (washerID <- washerIDList){
        var preparedStmt : PreparedStatement = connection.prepareStatement(washerRoomMapInsertQuery)
        preparedStmt.setInt(1, washerID)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.setInt(3, washer.getNumMinutesLeft)
        preparedStmt.setString(4, dryer.getRawStatus)
        preparedStmt.execute()
      }

      val dryerRoomMapInsertQuery = "INSERT INTO dryer_room_map (dryer_id, room_id, time_left, raw_status) VALUES (?, ?, ?, ?)"
      for (dryerID <- dryerIDList){
        var preparedStmt : PreparedStatement = connection.prepareStatement(dryerRoomMapInsertQuery)
        preparedStmt.setInt(1, dryerID)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.setInt(3, dryer.getNumMinutesLeft)
        preparedStmt.setString(4, dryer.getRawStatus)
        preparedStmt.execute()
      }
    }
  }

  def getToday() : String {
    val statement = connection.createStatement()
    var rs = statement.executeQuery("select last_insert_id() as last_id from rooms");

  }


}
