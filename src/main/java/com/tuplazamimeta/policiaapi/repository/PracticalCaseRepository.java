package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.PracticalCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PracticalCaseRepository extends JpaRepository<PracticalCase, Long> {
    List<PracticalCase> findAllByOrderByCreatedAtDesc();
}