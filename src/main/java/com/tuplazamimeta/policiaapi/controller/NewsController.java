package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.NewsResponse;
import com.tuplazamimeta.policiaapi.mapper.NewsMapper;
import com.tuplazamimeta.policiaapi.model.News;
import com.tuplazamimeta.policiaapi.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class NewsController {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    // Obtener todas las noticias (Público/Logueados)
    @GetMapping
    public List<NewsResponse> getAllNews() {
        return newsRepository.findAllByOrderByPublishedDateDesc()
                .stream()
                .map(newsMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Subir noticia (Solo Admin/Profesor)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<NewsResponse> createNews(@RequestBody News news) {
        if (news == null) {
            return ResponseEntity.badRequest().build();
        }
        News savedNews = newsRepository.save(news);
        return ResponseEntity.ok(newsMapper.toResponse(savedNews));
    }

    // Borrar noticia
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!newsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        newsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}