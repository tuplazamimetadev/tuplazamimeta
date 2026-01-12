package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.ContentResponse;
import com.tuplazamimeta.policiaapi.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping
    public List<ContentResponse> getAllContents() {
        return contentService.getAllContents();
    }
}