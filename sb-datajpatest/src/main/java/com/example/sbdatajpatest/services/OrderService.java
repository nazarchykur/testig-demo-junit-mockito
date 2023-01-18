package com.example.sbdatajpatest.services;



import com.example.sbdatajpatest.OrderAlreadyPaid;
import com.example.sbdatajpatest.Receipt;
import com.example.sbdatajpatest.entities.Order;
import com.example.sbdatajpatest.entities.Payment;
import com.example.sbdatajpatest.repositories.OrderRepository;
import com.example.sbdatajpatest.repositories.PaymentRepository;
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
            throw new OrderAlreadyPaid();
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
