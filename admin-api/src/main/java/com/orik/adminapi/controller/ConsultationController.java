package com.orik.adminapi.controller;

import com.orik.adminapi.data.DAO.ChatRepository;
import com.orik.adminapi.data.DAO.ConsultationRepository;
import com.orik.adminapi.data.DAO.OrderRepository;
import com.orik.adminapi.data.entity.Chat;
import com.orik.adminapi.data.entity.Consultation;
import com.orik.adminapi.data.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConsultationController {

    private ConsultationRepository orderRepository;

    @GetMapping("/test")
    public List<Consultation> getTestData(){
        return orderRepository.findAll();
    }

}
