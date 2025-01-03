package com.artkapl.new_webshop.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String manufacturer;

    private String description;

    @Positive
    private BigDecimal price;

    private int inventory;  // Quantity in Stock

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    // Constructor for creating products from API
    public Product(String name, String manufacturer, String description, BigDecimal price, int inventory, Category category) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }

}
