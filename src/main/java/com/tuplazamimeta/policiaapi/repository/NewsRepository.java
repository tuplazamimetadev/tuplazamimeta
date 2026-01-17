package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    // Obtener las noticias más recientes primero
    List<News> findAllByOrderByPublishedDateDesc();
}