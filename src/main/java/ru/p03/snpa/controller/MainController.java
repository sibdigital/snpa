package ru.p03.snpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.p03.snpa.entity.forms.SearchForm;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping("")
    private String main(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){

        return "redirect:search";
    }

    @GetMapping("/")
    private String main2(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm){

        return "redirect:search";
    }

}
