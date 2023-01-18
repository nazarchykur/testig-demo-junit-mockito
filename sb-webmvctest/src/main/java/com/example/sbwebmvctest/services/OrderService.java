package com.example.sbwebmvctest.services;


import com.example.sbwebmvctest.Receipt;
import com.example.sbwebmvctest.entities.Order;
import com.example.sbwebmvctest.entities.Payment;
import com.example.sbwebmvctest.exceptions.PaymentException;
import com.example.sbwebmvctest.repositories.OrderRepository;
import com.example.sbwebmvctest.repositories.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    public Payment pay(Long orderId, String creditCardNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        if (order.isPaid()) {
            throw new PaymentException();
        }
        orderRepository.save(order.markPaid());
        
        return paymentRepository.save(new Payment(order, creditCardNumber));
    }

    public Receipt getReceipt(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return new Receipt(payment.getOrder().getDate(), payment.getCreditCardNumber(), payment.getOrder().getAmount());
    }
}
