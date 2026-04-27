package ru.nssl.league_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.nssl.league_api.entity.News;
import ru.nssl.league_api.service.NewsService;

@Controller
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "НССЛ Джиу-джитсу — Национальная спортивная студенческая лига");
        model.addAttribute("newsList", newsService.getLatestNews(6));
        return "index";
    }

    @GetMapping("/news")
    public String newsList(Model model) {
        model.addAttribute("title", "Новости — НССЛ Джиу-джитсу");
        model.addAttribute("newsList", newsService.getAllNews());
        return "news";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("title", news.getTitle());
        model.addAttribute("news", news);
        return "news-detail";
    }
}