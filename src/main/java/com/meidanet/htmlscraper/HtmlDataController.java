package com.meidanet.htmlscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://www.mta.ac.il")
@RequestMapping("/api")
public class HtmlDataController {

    @Autowired
    private HtmlDataRepository htmlDataRepository;

    @PostMapping("/saveData")
    public HtmlData saveData(@RequestBody HtmlData data) {

        return htmlDataRepository.save(data);
    }

}
