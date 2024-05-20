package com.orik.adminapi.controller.api;

import com.orik.adminapi.data.DAO.OrderRepository;
import com.orik.adminapi.data.DAO.UserRepository;
import com.orik.adminapi.data.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepository.findById(id).get();
    }

    @GetMapping("/users")
    public String getUsers(){
        return userRepository.findAll().get(0).getRole().getName();
    }


}
