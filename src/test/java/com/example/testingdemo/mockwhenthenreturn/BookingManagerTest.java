package com.example.testingdemo.mockwhenthenreturn;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookingManagerTest {

    private HotelDao hotelDaoMock;
    private BookingManager bookingManager;

    @Before
    public void setUp() {
        hotelDaoMock = mock(HotelDao.class);
        bookingManager = new BookingManager(hotelDaoMock);
    }

    @Test
    public void checkRoomAvailability_true() {
        List<String> availableRooms = Arrays.asList("A", "B");

        when(hotelDaoMock.fetchAvailableRooms()).thenReturn(availableRooms);

        assertTrue(bookingManager.checkRoomAvailability("A"));

        verify(hotelDaoMock, only()).fetchAvailableRooms();
    }

    @Test
    public void checkRoomAvailability_false() {
        List<String> availableRooms = Arrays.asList("A", "B");

        when(hotelDaoMock.fetchAvailableRooms()).thenReturn(availableRooms);

        assertFalse(bookingManager.checkRoomAvailability("C"));

        verify(hotelDaoMock, only()).fetchAvailableRooms();
    }
}