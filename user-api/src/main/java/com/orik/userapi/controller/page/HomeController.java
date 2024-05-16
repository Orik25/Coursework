package com.orik.userapi.controller.page;

import com.orik.userapi.DTO.OrderDTO;
import com.orik.userapi.DTO.RequestConsultationDTO;
import com.orik.userapi.constant.ServerType;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @GetMapping
    public String getHomePage(Model model){
        model.addAttribute("request",new RequestConsultationDTO());
        model.addAttribute("order",new OrderDTO());
        model.addAttribute("serverTypes", ServerType.values());

        return "home";
    }

    @PostMapping("/send-consult-request")
    public String sendConsultationRequest(@Valid @ModelAttribute("request") RequestConsultationDTO requestConsultationDTO,
                                          BindingResult theBindingResult){
        if(theBindingResult.hasErrors()){
            return "redirect:/";
        }
        else {
            System.out.println(requestConsultationDTO.getPhoneNumber());
            System.out.println(requestConsultationDTO.getMessage());
            return "redirect:/";
        }

    }

    @PostMapping("/send-order-request")
    public String sendOrderRequest(@Valid @ModelAttribute("order") OrderDTO orderDTO,
                                          BindingResult theBindingResult){
        if(theBindingResult.hasErrors()){
            return "redirect:/";
        }
        else {
            System.out.println(orderDTO.getFirstName());
            System.out.println(orderDTO.getLastName());
            System.out.println(orderDTO.getPhoneNumber());
            System.out.println(orderDTO.getEmail());
            System.out.println(orderDTO.getServerType().name());
            System.out.println(orderDTO.getMessage());
            return "redirect:/";
        }

    }
}
