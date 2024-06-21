package com.meidanet.htmlscraper.corp;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Here you can return a specific error page or handle errors programmatically
        return "error"; // Assuming you have an 'error.html' page in your templates folder
    }
}
