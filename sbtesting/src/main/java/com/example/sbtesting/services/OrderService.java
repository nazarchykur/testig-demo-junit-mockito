package com.example.sbtesting.services;

import com.example.sbtesting.entities.Order;
import com.example.sbtesting.entities.Payment;
import com.example.sbtesting.exceptions.PaymentException;
import com.example.sbtesting.repositories.OrderRepository;
import com.example.sbtesting.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    public Payment pay(Long orderId, String creditCardNumber) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        if (order.isPaid()) {
            throw new PaymentException();
        }
        orderRepository.save(order.markPaid());
        
        return paymentRepository.save(new Payment(order, creditCardNumber));
    }
}
