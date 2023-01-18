package com.example.sbdatajpatest.repositories;


import com.example.sbdatajpatest.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}