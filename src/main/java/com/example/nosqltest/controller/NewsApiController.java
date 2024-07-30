package com.example.nosqltest.controller;

import com.example.nosqltest.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/news")
@Controller
@RequiredArgsConstructor
public class NewsApiController {

    private final NewsApiService newsApiService;

    @ResponseBody
    @GetMapping
    public String all(){
        return "This is nosql test";
    }

    @ResponseBody
    @GetMapping("/")
    public String all2(){
        return "This is nosql test";
    }

    @ResponseBody
    @GetMapping("/ev")
    public String getEverything(){
        return newsApiService.getEverything();
    }

    @ResponseBody
    @GetMapping("/top")
    public String getTop(){
        return newsApiService.getTopHeadlines();
    }

    @ResponseBody
    @GetMapping("/getArticle")
    public String getArticle(){
        return newsApiService.getArticle();
    }

}
