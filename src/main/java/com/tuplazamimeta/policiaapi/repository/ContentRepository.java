package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}