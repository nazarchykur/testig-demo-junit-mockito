package com.example.testingdemo.mockwhenthenreturn;

import java.util.ArrayList;
import java.util.List;

public class HotelDao {

    public List<String> fetchAvailableRooms(){
        List<String> availableRooms = new ArrayList<String>();
        availableRooms.add("Room 1");
        availableRooms.add("Room 2");
        availableRooms.add("Room expensive 1");
        availableRooms.add("Room expensive 2");
        
        return availableRooms;
    }
    
//    public List<String> fetchAvailableRooms2() throws SQLException {
//        List<String> availableRooms = new ArrayList<String>();
//        Connection conn = DriverManager.getConnection("DATABASE_URL");
//        Statement statement = conn.createStatement();
//        ResultSet rs;
//        rs = statement.executeQuery("SELECT * FROM ROOMS WHERE AVAILABLE like '1'");
//        while(rs.next()){
//            availableRooms.add(rs.getString("Room name"));
//        }
//        return availableRooms;
//    }
}
