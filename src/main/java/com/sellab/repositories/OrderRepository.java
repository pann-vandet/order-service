package com.sellab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sellab.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
