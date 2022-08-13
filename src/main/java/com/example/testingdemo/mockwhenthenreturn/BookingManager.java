package com.example.testingdemo.mockwhenthenreturn;

import java.util.List;

public class BookingManager {
    private HotelDao dao;

    public BookingManager(HotelDao dao) {
        this.dao = dao;
    }

    public boolean checkRoomAvailability(String roomName) {

        List<String> roomsAvailable = dao.fetchAvailableRooms();
        return roomsAvailable.contains(roomName);
    }
}
