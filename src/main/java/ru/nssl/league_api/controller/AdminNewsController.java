package ru.nssl.league_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nssl.league_api.dto.NewsCreateRequest;
import ru.nssl.league_api.entity.News;
import ru.nssl.league_api.service.NewsService;

import java.util.List;

@Controller
@RequestMapping("/admin/news")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NewsService newsService;

    @GetMapping
    public String page() {
        return "admin-news";
    }

    @PostMapping
    public String create(@ModelAttribute NewsCreateRequest request,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        News news = new News();

        news.setTitle(request.getTitle());
        news.setShortDescription(request.getShortDescription());
        news.setExternalLink(request.getExternalLink());

        newsService.save(news, files);

        return "redirect:/admin/news";
    }
}