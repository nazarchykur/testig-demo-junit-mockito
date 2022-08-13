package com.example.testingdemo.mockitoverify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TrainBarriersTest {
    
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TrainBarriers serviceUnderTest;

    public TrainBarriersTest() {
    }

    @Test
    public void passengerEntry() {
        final int passengerId = 3;

        serviceUnderTest.passengerEntry(passengerId);
        
        /*
             У verify ми можемо перевірити чи такий-то метод з такого-то класу викликався
             якщо так, то скільки разів і з якими параметрами
             
                 Якщо там будуть передані інші параметри, то ми отримаємо помилку:
                    - argument(s) are different!...
                 
                 Якщо такий-то метод НЕ викликався, то також отримаємо помилку:
                    - wanted but not invoked: className.methodName(param1, param2) ...
         */
        
        verify(passengerRepository, only()).registerPassengerOnTrain(passengerId);
        verify(emailService, only()).notifyPassenger(passengerId);
    }
}