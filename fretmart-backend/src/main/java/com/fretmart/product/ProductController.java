package com.fretmart.product;

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
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(service.getAllProducts(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id){
        return ResponseEntity.ok(service.getProductById(id));
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        Product savedProduct = service.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @Valid @RequestBody Product product){
        return ResponseEntity.ok(service.updateProduct(id,product));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id){
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Product>> getProductByCategory(@PathVariable String category,@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(service.getProductsByCategory(category,pageable));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProduct(@RequestParam String q,@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(service.searchProducts(q,pageable));
    }
}
