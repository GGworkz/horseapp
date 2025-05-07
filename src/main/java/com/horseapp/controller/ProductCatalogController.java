package com.horseapp.controller;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.horseapp.dto.ProductCatalogCreateDTO;
import com.horseapp.dto.ProductCatalogPriceUpdateDTO;
import com.horseapp.dto.ProductCatalogUpdateDTO;
import com.horseapp.model.ProductCatalog;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.ProductCatalogService;
import com.horseapp.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Catalog", description = "User product catalog management")
@RestController
@RequestMapping("/user/{userId}/catalogs")
public class ProductCatalogController {

    private final ProductCatalogService catalogService;
    private final AuthorizationService authorizationService;
    private final UserService userService;

    public ProductCatalogController(ProductCatalogService catalogService,
                                    AuthorizationService authorizationService,
                                    UserService userService) {
        this.catalogService = catalogService;
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @GetMapping
    public ResponseEntity<?> getCatalogs(@PathVariable Long userId) {
        return ResponseEntity.ok(catalogService.getCatalogsForUser(userId));
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @PostMapping
    public ResponseEntity<?> createCatalog(@PathVariable Long userId,
                                           @Valid @RequestBody ProductCatalogCreateDTO dto) {
        ProductCatalog catalog = new ProductCatalog();
        catalog.setName(dto.getName());
        catalog.setType(dto.getType());
        catalog.setPrice(dto.getPrice());
        catalog.setUser(userService.findById(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.create(catalog));
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @PutMapping("/{catalogId}")
    public ResponseEntity<?> updateCatalog(@PathVariable Long userId,
                                           @PathVariable Long catalogId,
                                           @Valid @RequestBody ProductCatalogUpdateDTO catalogDTO) {
        Optional<ProductCatalog> existingOpt = catalogService.getById(catalogId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found");
        }

        ProductCatalog existingCatalog = existingOpt.get();
        existingCatalog.setName(catalogDTO.getName());
        existingCatalog.setType(catalogDTO.getType());
        existingCatalog.setPrice(catalogDTO.getPrice());
        existingCatalog.setUser(userService.findById(userId)); // preserve association

        catalogService.update(existingCatalog);
        return ResponseEntity.ok("Catalog updated successfully");
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @DeleteMapping("/{catalogId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable Long userId,
                                           @PathVariable Long catalogId) {
        Optional<ProductCatalog> existing = catalogService.getById(catalogId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found");
        }

        catalogService.delete(catalogId);
        return ResponseEntity.ok("Catalog deleted");
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @PutMapping("/{catalogId}/price")
    public ResponseEntity<?> updateCatalogPrice(@PathVariable Long userId,
                                                @PathVariable Long catalogId,
                                                @RequestBody ProductCatalogPriceUpdateDTO catalogUpdate) {
        Optional<ProductCatalog> existing = catalogService.getById(catalogId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found");
        }

        ProductCatalog catalog = existing.get();
        catalog.setPrice(catalogUpdate.getPrice());

        return ResponseEntity.ok(catalogService.update(catalog));
    }
}
