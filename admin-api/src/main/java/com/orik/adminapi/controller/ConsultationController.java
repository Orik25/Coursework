package com.orik.adminapi.controller;

import com.orik.adminapi.data.service.interfaces.ConsultationsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class ConsultationController {

    private ConsultationsService consultationsService;

    @GetMapping("/consultations")
    public String getConsultations(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "1") int size,
                             @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder){
        model.addAttribute("consultationsPage", consultationsService.getAllConsultationsSorted(page,size,sortField, sortOrder));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "admin/consultations";
    }

    @GetMapping("/search-consultations")
    public String searchDriversByLastName(@RequestParam(name = "searchField") String searchField,
                                          @RequestParam(name = "searchValue") String searchValue,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "1") int size,
                                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("searchField", searchField);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("consultationsPage", consultationsService.findByFieldContainingIgnoreCase(searchField,searchValue, pageable));
        return "admin/consultations";
    }

    @GetMapping("/consultations/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        consultationsService.deleteById(id);
        return "redirect:/admin/consultations";
    }
}
