package com.example.sbwebmvctest.repositories;


import com.example.sbwebmvctest.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}