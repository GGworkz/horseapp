package com.horseapp.controller;

import com.horseapp.model.ProductCatalog;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.ProductCatalogService;
import com.horseapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Product Catalog", description = "User-scoped product catalog management")
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
    public ResponseEntity<?> createCatalog(@PathVariable Long userId, @RequestBody ProductCatalog catalog) {
        catalog.setUser(userService.findById(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.create(catalog));
    }

    @PreAuthorize("@accessGuard.hasUserAccess(#userId)")
    @PutMapping("/{catalogId}")
    public ResponseEntity<?> updateCatalog(@PathVariable Long userId,
                                           @PathVariable Long catalogId,
                                           @RequestBody ProductCatalog catalog) {
        Optional<ProductCatalog> existing = catalogService.getById(catalogId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found");
        }

        catalog.setId(catalogId);
        catalog.setUser(userService.findById(userId));
        return ResponseEntity.ok(catalogService.update(catalog));
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
}
