package com.fretmart.product.dto;

import com.fretmart.product.Product;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ProductResponseDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    String category;
    Integer stock;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static ProductResponseDto fromEntity(Product product){
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}