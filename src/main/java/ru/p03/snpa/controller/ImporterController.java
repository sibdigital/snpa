package ru.p03.snpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImporterController {

    @GetMapping("/importer")
    public String importer(){

        return "importer";

    }



}
