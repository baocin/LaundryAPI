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
      var numAvailableWashers = 0;
      var numWashers = 0;
      for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
        if (washer.getRawStatus == "avail") numAvailableWashers+=1;
        numWashers+=1;
      }

      var numAvailableDryers = 0;
      var numDryers = 0;
      for (dryer <- find.getDryers(hall.getHallName, hall.getFloorNumber.toString)){
        if (dryer.getRawStatus == "avail") numAvailableDryers+=1;
        numDryers+=1;
      }

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
        val roomUpdateQuery = s"""UPDATE rooms SET num_available_washers=${numAvailableWashers}, num_available_dryers=${numAvailableDryers}, washer_count=${numWashers}, dryer_count=${numDryers} WHERE url="${hall.getUrl}""""
        var preparedStmt : PreparedStatement = connection.prepareStatement(roomUpdateQuery)
        preparedStmt.execute()
      }else{    //make new
        println(s"making new room ${hall.getHallName} ${hall.getFloorNumber}")
        val roomInsertQuery = "INSERT INTO rooms (floor_number, hall_name, url, num_available_washers, num_available_dryers,washer_count,dryer_count) VALUES (?,?,?,?,?,?,?)"
        var preparedStmt : PreparedStatement = connection.prepareStatement(roomInsertQuery)
        preparedStmt.setInt(1, hall.getFloorNumber)
        preparedStmt.setString(2, hall.getHallName)
        preparedStmt.setString(3, hall.getUrl)
        preparedStmt.setInt(4, numAvailableWashers)
        preparedStmt.setInt(5, numAvailableDryers)
        preparedStmt.setInt(6, numWashers)
        preparedStmt.setInt(7, numDryers)
        preparedStmt.execute()

        var rs = statement.executeQuery("select last_insert_id() as last_id from rooms");
        rs.first()
        hallRowID = rs.getString("last_id").toInt;
      }

      val washerInsertQuery = "INSERT INTO washers (name, time_left, available, raw_status) VALUES (?,?,?,?)"
      var washerIDList = new ArrayList[Int]()
      for (washer <- find.getWashers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(washerInsertQuery)
        preparedStmt.setString(1, washer.getName)
        preparedStmt.setInt(2, washer.getNumMinutesLeft)
        preparedStmt.setBoolean(3,  (if (washer.getRawStatus == "avail") true else false))
        preparedStmt.setString(4, washer.getRawStatus)
        preparedStmt.execute()
        var rs = statement.executeQuery("select last_insert_id() as last_id from washers");
        rs.first()
        var lastid = rs.getString("last_id");
        washerIDList.append(lastid.toInt)
      }

      var dryerIDList = new ArrayList[Int]()
      val dryerInsertQuery = "INSERT INTO dryers (name, time_left, available, raw_status) VALUES (?,?,?,?)"
      for (dryer <- find.getDryers(hall.getHallName, hall.getFloorNumber.toString)){
        var preparedStmt : PreparedStatement = connection.prepareStatement(dryerInsertQuery)
        preparedStmt.setString(1, dryer.getName)
        preparedStmt.setInt(2, dryer.getNumMinutesLeft)
        preparedStmt.setBoolean(3, (if (dryer.getRawStatus == "avail") true else false))
        preparedStmt.setString(4, dryer.getRawStatus)
        preparedStmt.execute()

        var rs = statement.executeQuery("select last_insert_id() as last_id from dryers");
        rs.first()
        var lastid = rs.getString("last_id");
        dryerIDList.append(lastid.toInt)
      }

      val washerRoomMapInsertQuery = "INSERT INTO washer_room_map (washer_id, room_id) VALUES (?,?)"
      for (washerID <- washerIDList){
        var preparedStmt : PreparedStatement = connection.prepareStatement(washerRoomMapInsertQuery)
        preparedStmt.setInt(1, washerID)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.execute()
      }

      val dryerRoomMapInsertQuery = "INSERT INTO dryer_room_map (dryer_id, room_id) VALUES (?,?)"
      for (dryerID <- dryerIDList){
        var preparedStmt : PreparedStatement = connection.prepareStatement(dryerRoomMapInsertQuery)
        preparedStmt.setInt(1, dryerID)
        preparedStmt.setInt(2, hallRowID)
        preparedStmt.execute()
      }

      // val dryerRoomMapInsertQuery = "INSERT INTO dryer_room_map (dryer_id, room_id) VALUES (?,?)"
      // var dryerResult = statement.executeQuery("SELECT * FROM dryers");
      // while(dryerResult.next()){
      //   var dryerID = dryerResult.getInt("dryer_id")
      //   var preparedStmt : PreparedStatement = connection.prepareStatement(dryerRoomMapInsertQuery)
      //   preparedStmt.setInt(1, dryerID)
      //   preparedStmt.setInt(2, hallRowID)
      //   preparedStmt.execute()
      // }

    }
  }


}
