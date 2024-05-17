package com.orik.adminapi.data.DAO;

import com.orik.adminapi.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {}