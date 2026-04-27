package ru.nssl.league_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.nssl.league_api.enums.NewsCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news")
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String shortDescription;

    private String externalLink;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsFile> files = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}