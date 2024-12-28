package com.artkapl.new_webshop.request;

import java.math.BigDecimal;

import com.artkapl.new_webshop.model.Category;

import lombok.Data;

@Data
public class ProductCreateRequest {
    private Long id;
    private String name;
    private String manufacturer;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;

}
