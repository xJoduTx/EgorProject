package ru.nssl.league_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nssl.league_api.entity.News;
import ru.nssl.league_api.entity.NewsFile;
import ru.nssl.league_api.enums.FileType;
import ru.nssl.league_api.service.NewsService;

import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequiredArgsConstructor
public class FileController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private final NewsService newsService;

    // =========================
    // 1. СКАЧАТЬ ОДИН ФАЙЛ
    // =========================
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {

        Path path = Paths.get(uploadDir)
                .toAbsolutePath()
                .resolve(filename)
                .normalize();

        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + filename);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }

    // =========================
    // 2. СКАЧАТЬ ВСЕ ФАЙЛЫ НОВОСТИ (ZIP)
    // =========================
    @GetMapping("/files/news/{id}/download-all")
    public ResponseEntity<Resource> downloadAll(@PathVariable Long id) {

        try {
            News news = newsService.getNewsById(id);

            List<NewsFile> files = news.getFiles();

            if (files == null || files.isEmpty()) {
                throw new RuntimeException("No files for news id " + id);
            }

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            for (NewsFile file : files) {

                // 🔥 ВАЖНО: только документы, НЕ картинки
                if (file.getType() != FileType.FILE) continue;

                Path filePath = uploadPath.resolve(file.getFilePath());

                if (!Files.exists(filePath)) continue;

                ZipEntry entry = new ZipEntry(file.getFilePath());
                zos.putNextEntry(entry);

                Files.copy(filePath, zos);

                zos.closeEntry();
            }

            zos.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"news-" + id + "-files.zip\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/zip")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Zip creation failed", e);
        }
    }}