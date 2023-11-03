package com.Bil429Project.pingTraceroutApp.controller;

import com.Bil429Project.pingTraceroutApp.entity.PingResult;
import com.Bil429Project.pingTraceroutApp.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
@Controller
public class WebController {

    @Autowired
    private PingService pingService;

    @PostMapping("/ping")
    public String ping(@RequestParam String target, Model model) {
        PingResult result = pingService.ping(target);
        model.addAttribute("result", result);
        return "ping";
    }

    @GetMapping("/")
    public String index() { //ana(ilk) sayfaya yönlendirme
        return "index";
    }

}