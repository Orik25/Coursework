package com.orik.adminapi.controller;

import com.orik.adminapi.DTO.OrderUpdateDTO;
import com.orik.adminapi.constant.order.ServerType;
import com.orik.adminapi.constant.order.StatusType;
import com.orik.adminapi.data.service.interfaces.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping("/orders")
    public String getOrders(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "1") int size,
                            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        model.addAttribute("ordersPage", ordersService.getAllOrdersSorted(page, size, sortField, sortOrder));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("orderUpdate", new OrderUpdateDTO());
        model.addAttribute("statusTypes", StatusType.values());
        model.addAttribute("serverTypes", ServerType.values());
        return "admin/orders";
    }

    @GetMapping("/search-orders")
    public String searchDriversByLastName(@RequestParam(name = "searchField") String searchField,
                                          @RequestParam(name = "searchValue") String searchValue,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "1") int size,
                                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("orderUpdate", new OrderUpdateDTO());
        model.addAttribute("statusTypes", StatusType.values());
        model.addAttribute("serverTypes", ServerType.values());
        model.addAttribute("ordersPage", ordersService.findByFieldContainingIgnoreCase(searchField, searchValue, pageable));
        return "admin/orders";
    }

    @PostMapping("/orders/update")
    public String updateOrder(@ModelAttribute("orderUpdate") OrderUpdateDTO orderUpdate){
        ordersService.update(orderUpdate);
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrderById(@PathVariable Long id){
        ordersService.deleteById(id);
        return "redirect:/admin/orders";
    }

}
