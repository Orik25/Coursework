package com.orik.adminapi.controller.api;

import com.orik.adminapi.data.DAO.OrderRepository;
import com.orik.adminapi.data.entity.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepository.findById(id).get();
    }


}
