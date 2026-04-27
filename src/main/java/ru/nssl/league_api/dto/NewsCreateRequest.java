package ru.nssl.league_api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.nssl.league_api.enums.NewsCategory;

@Data
public class NewsCreateRequest {

    private String title;
    private String text;
    private String shortDescription;
    private String externalLink;
    private NewsCategory category;

    private MultipartFile[] files;
}