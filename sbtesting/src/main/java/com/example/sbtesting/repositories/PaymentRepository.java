package com.example.sbtesting.repositories;

import com.example.sbtesting.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}