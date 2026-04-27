package ru.nssl.league_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class NewsAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;
    private String originalName;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
}