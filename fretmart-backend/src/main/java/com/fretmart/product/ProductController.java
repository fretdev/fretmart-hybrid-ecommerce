package com.fretmart.product;

import com.fretmart.dto.ProductRequestDto;
import com.fretmart.dto.ProductResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@PageableDefault(size = 10)Pageable pageable){
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductResponseDto> response = products.map(ProductResponseDto::fromEntity);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id ){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ProductResponseDto.fromEntity(product));
    }
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request){
        Product savedProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponseDto.fromEntity(savedProduct));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request){
        return ResponseEntity.ok(ProductResponseDto.fromEntity(productService.updateProduct(id,request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@PathVariable String category,@PageableDefault(size = 10) Pageable pageable){
        Page<Product> products = productService.getProductsByCategory(category,pageable);
        Page<ProductResponseDto> response = products.map(ProductResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProduct(@RequestParam String q,@PageableDefault(size = 10) Pageable pageable){
        Page<Product> products = productService.searchProducts(q,pageable);
        Page<ProductResponseDto> response = products.map(ProductResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }
}
