package edu.nus.iss.sg.vttp_workflow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.nus.iss.sg.vttp_workflow.services.GifServices;

@Controller
public class GifController {

    @Autowired
    private GifServices gifSvc;
    
    @PostMapping(path="/postGif")
    public String postGif(Model model, @RequestBody MultiValueMap<String, String> body) {

        String query = body.getFirst("phrase");
        String limit = body.getFirst("limit");
        String rating = body.getFirst("rating");

        model.addAttribute("phrase", query);
        model.addAttribute("limit", limit);
        model.addAttribute("rating", rating);

        Integer iLimit = Integer.parseInt(limit);

        List<String> images = gifSvc.searchGifImages(query, iLimit, rating);
        model.addAttribute("images", images);

        return "result";
    }
}
