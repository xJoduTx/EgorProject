package ru.nssl.league_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nssl.league_api.entity.News;
import ru.nssl.league_api.entity.NewsFile;
import ru.nssl.league_api.enums.FileType;
import ru.nssl.league_api.repository.NewsRepository;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public News save(News news, List<MultipartFile> files) {

        List<NewsFile> fileEntities = new ArrayList<>();

        try {
            Path uploadPath = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (files != null) {
                for (MultipartFile file : files) {

                    if (file == null || file.isEmpty()) continue;

                    String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";

                    String safeName = originalName
                            .replaceAll("[—–«»\"']", "-")           // заменяем проблемные тире и кавычки
                            .replaceAll("[^a-zA-Z0-9._-]", "_");    // оставляем только безопасные символы

                    String filename = UUID.randomUUID()
                            + "_" + file.getOriginalFilename();

                    Path target = uploadPath.resolve(filename);

                    file.transferTo(target.toFile());

                    NewsFile nf = new NewsFile();
                    nf.setFilePath(filename);

                    String contentType = file.getContentType();

                    if (contentType != null && contentType.startsWith("image")) {
                        nf.setType(FileType.IMAGE);
                    } else {
                        nf.setType(FileType.FILE);
                    }

                    nf.setNews(news);
                    fileEntities.add(nf);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("File upload error", e);
        }

        news.setFiles(fileEntities);

        return newsRepository.save(news);
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public List<News> getLatestNews(int limit) {
        return newsRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(limit)
                .toList();
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }
}