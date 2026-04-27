package ru.nssl.league_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class NewsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
}