package com.fretmart.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    @Size(min = 2,max = 255,message = "Product name must be between 2 and 255 characters")
    private String name;

    @Size(max = 5000,message = "Description cannot exceed 5000 characters")
    private String description;

    @Pattern(regexp = "^(Electronics|Clothing|Books|Home|Sports|Toys|Other)?$",message = "Category must be one of: Electronics, Clothing, Books, Home, Sports, Toys, Other")
    private String category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @DecimalMax(value = "9999999.99",message = "Price cannot exceed 9,999,999,.99")
    private BigDecimal price;

    @Min(value = 0,message = "Stock cannot be negative")
    @Max(value = 999999,message = "Stock cannot exceed 999,999")
    private Integer stock;

    @URL(message = "Image URL must be a valid URL")
    @Size(max = 500,message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
}
