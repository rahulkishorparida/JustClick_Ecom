package com.jc.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jc.demo.model.FormResponse;
import com.jc.demo.service.FormService;
@Controller
@RequestMapping("/form")
public class FormController {
	

    @Autowired
    private FormService formService;

    @GetMapping("/forms")
    public String getForms(Model model) {
        List<FormResponse> forms = formService.getAllForms();
        model.addAttribute("forms", forms);
        return "fom"; // Thymeleaf template name (forms.html)
    }



}
