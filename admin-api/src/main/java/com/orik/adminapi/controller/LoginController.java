package com.orik.adminapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(name = "successRegister",defaultValue = "false") boolean success,
                                Model model){
        model.addAttribute("success" ,success);
        return "login/login";
    }

}