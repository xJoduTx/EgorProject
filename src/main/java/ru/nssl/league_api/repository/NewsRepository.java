package ru.nssl.league_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nssl.league_api.entity.News;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findTop6ByOrderByCreatedAtDesc();

    List<News> findAllByOrderByCreatedAtDesc();

    Optional<News> findById(Long id);
}