package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}