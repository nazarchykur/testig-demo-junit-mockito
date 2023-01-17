package com.example.sbtesting.repositories;

import com.example.sbtesting.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}