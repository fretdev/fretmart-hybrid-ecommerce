package com.fretmart.product;


import com.fretmart.dto.ProductRequestDto;
import com.fretmart.exception.DuplicateProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fretmart.exception.ProductNotFoundException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Transactional
    public Product createProduct(ProductRequestDto request){
        productRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing->{
            throw new DuplicateProductException(request.getName());
        });
        Product product = convertToEntity(request);
        return  productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id,ProductRequestDto request){
        Product existingProduct = getProductById(id);
        productRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing->{
            if(!existing.getId().equals(id)){
                throw new DuplicateProductException(request.getName());
            }
        });

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setImageUrl(request.getImageUrl());

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(String category,Pageable pageable){
        if(category == null || category.isBlank()){
            return new PageImpl<>(Collections.emptyList(),pageable,0);
        }
        return productRepository.findByCategoryIgnoreCase(category,pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword,Pageable pageable){
        if(keyword == null || keyword.isBlank()){
            return new PageImpl<>(Collections.emptyList(),pageable,0);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword,pageable);
    }

    private Product convertToEntity(ProductRequestDto request){
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        return product;
    }
}
