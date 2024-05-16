package com.orik.userapi.data.DAO;

import com.orik.userapi.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {}