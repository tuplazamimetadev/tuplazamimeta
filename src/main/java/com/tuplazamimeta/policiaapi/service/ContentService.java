package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.dto.response.ContentResponse;
import com.tuplazamimeta.policiaapi.mapper.ContentMapper;
import com.tuplazamimeta.policiaapi.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;

    public List<ContentResponse> getAllContents() {
        return contentMapper.toResponseList(contentRepository.findAll());
    }
}