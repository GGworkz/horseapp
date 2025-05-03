package com.horseapp.service;

import com.horseapp.model.ProductCatalog;
import com.horseapp.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCatalogService {

    private final ProductCatalogRepository catalogRepository;

    public ProductCatalogService(ProductCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<ProductCatalog> getCatalogsForUser(Long userId) {
        return catalogRepository.findByUserId(userId);
    }

    public Optional<ProductCatalog> getById(Long id) {
        return catalogRepository.findById(id);
    }

    public ProductCatalog create(ProductCatalog catalog) {
        return catalogRepository.save(catalog);
    }

    public ProductCatalog update(ProductCatalog catalog) {
        return catalogRepository.save(catalog);
    }

    public void delete(Long id) {
        catalogRepository.deleteById(id);
    }
}
