package ru.nssl.league_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.nssl.league_api.enums.FileType;

@Entity
@Data
public class NewsFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileType type;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;
}
