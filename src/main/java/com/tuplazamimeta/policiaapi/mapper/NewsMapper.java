package com.tuplazamimeta.policiaapi.mapper;

import com.tuplazamimeta.policiaapi.model.News;
import com.tuplazamimeta.policiaapi.dto.response.NewsResponse;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {
    public NewsResponse toResponse(News news) {
        if (news == null) return null;
        NewsResponse response = new NewsResponse();
        response.setId(news.getId());
        response.setTitle(news.getTitle());
        response.setDescription(news.getDescription());
        response.setLink(news.getLink());
        response.setPublishedDate(news.getPublishedDate());
        return response;
    }
}