package com.horseapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@Table(name = "product_catalogs")
public class ProductCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_catalog_seq_gen")
    @SequenceGenerator(name = "product_catalog_seq_gen", sequenceName = "product_catalog_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String type;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCatalog)) return false;
        ProductCatalog that = (ProductCatalog) o;
        return Objects.equals(name, that.name)
                && Objects.equals(type, that.type)
                && Objects.equals(price, that.price)
                && Objects.equals(user != null ? user.getId() : null,
                that.user != null ? that.user.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, price);
    }
}
