package com.orik.adminapi.controller;

import com.orik.adminapi.DTO.MessageDTO;
import com.orik.adminapi.service.interfaces.MessageSender;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class MessageController {

    private MessageSender messageSender;

    @GetMapping("/send-message")
    public String showSendMessageForm(Model model) {
        model.addAttribute("messageForm", new MessageDTO());
        return "admin/message";
    }

    @PostMapping(path = "/send-message", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String sendMessage(@ModelAttribute("messageForm") MessageDTO message,
                              RedirectAttributes redirectAttributes) {
        MultipartFile file = message.getFile();
        if (!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                if (filename != null && !filename.isEmpty()) {
                    int lastDotIndex = filename.lastIndexOf('.');
                    if (lastDotIndex != -1) {
                        String fileExtension = filename.substring(lastDotIndex + 1).toLowerCase();
                        if (!Arrays.asList("jpg", "jpeg", "png").contains(fileExtension)) {
                            redirectAttributes.addFlashAttribute("errorMessage", "You can attach only photos!");
                            return "redirect:/admin/send-message";
                        }
                    }
                }
                byte[] bytes = file.getBytes();
                messageSender.sendMessage(message.getMessage(),bytes);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "The file could not be downloaded");
                return "redirect:/admin/send-message";
            }
        }else{
            messageSender.sendMessage(message.getMessage());
        }

        redirectAttributes.addFlashAttribute("successMessage", "Message successfully sent!");
        return "redirect:/admin/send-message";
    }
}
