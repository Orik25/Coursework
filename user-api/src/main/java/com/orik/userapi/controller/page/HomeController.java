package com.orik.userapi.controller.page;

import com.orik.userapi.DTO.OrderDTO;
import com.orik.userapi.DTO.RequestConsultationDTO;
import com.orik.userapi.constant.ServerType;
import com.orik.userapi.data.DAO.ConsultationRepository;
import com.orik.userapi.data.DAO.OrderRepository;
import com.orik.userapi.data.entity.Consultation;
import com.orik.userapi.data.entity.Order;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private OrderRepository orderRepository;
    private ConsultationRepository consultationRepository;
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
            Consultation consultation = new Consultation();
            consultation.setPhoneNumber(requestConsultationDTO.getPhoneNumber());
            consultation.setMessage(requestConsultationDTO.getMessage());
            consultationRepository.save(consultation);
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
            Order order = new Order();
            order.setFirstName(orderDTO.getFirstName());
            order.setLastName(orderDTO.getLastName());
            order.setPhoneNumber(orderDTO.getPhoneNumber());
            order.setEmail(orderDTO.getEmail());
            order.setServerType(orderDTO.getServerType().name());
            order.setMessage(orderDTO.getMessage());
            order.setStatus("ACTIVE");
            orderRepository.save(order);
            return "redirect:/";
        }

    }
}
