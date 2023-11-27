package com.Bil429Project.pingTraceroutApp.controller;

import com.Bil429Project.pingTraceroutApp.entity.PingResult;
import com.Bil429Project.pingTraceroutApp.entity.TracerouteResult;
import com.Bil429Project.pingTraceroutApp.service.PingService;
import com.Bil429Project.pingTraceroutApp.service.TracerouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
@Controller
public class WebController {

    @Autowired
    private PingService pingService;

    @Autowired
    private TracerouteService tracerouteService;

    @GetMapping("/")
    public String index() { //ana(ilk) sayfaya yönlendirme
        return "index";
    }

   @PostMapping("/ping")
   public String ping(@RequestParam String target, Model model) {
       PingResult result = pingService.ping(target);
       model.addAttribute("result", result);
       if(result.getErrorMessage() != null){
           return "index";
       }else{
           return "ping";
       }
   }
    @PostMapping("/traceroute")
    public String traceroute(@RequestParam String target, Model model) {
        TracerouteResult tracerouteResult = tracerouteService.performTraceroute(target);
       model.addAttribute("tracerouteResult", tracerouteResult);
       if(tracerouteResult.getErrorMessage() != null){
            return "index";
        }else{
            return "traceroute";
       }
    }

}
