package com.horseapp.repository;

import java.util.List;

import com.horseapp.model.ProductCatalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, Long> {
    List<ProductCatalog> findByUserId(Long userId);
}
