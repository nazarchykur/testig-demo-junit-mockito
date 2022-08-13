package com.example.testingdemo.mockwhenthenreturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingManagerTest_usingJUnit5 {

    @Mock
    private HotelDao hotelDaoMock;
    @InjectMocks
    private BookingManager bookingManager;


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