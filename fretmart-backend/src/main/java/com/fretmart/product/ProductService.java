package com.fretmart.product;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable){
        return repo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(long id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("Product not found  with id : "+id));
    }

    @Transactional
    public Product createProduct(Product product){
        return  repo.save(product);
    }

    @Transactional
    public Product updateProduct(long id,Product updatedProduct){
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        return existingProduct;
    }

    @Transactional
    public void deleteProduct(long id){
//        if(!repo.existsById(id)){
//            throw new ProductNotFoundException("Product not found with id: "+id);
//        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(String category,Pageable pageable){
        return repo.findByCategoryIgnoreCase(category,pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword,Pageable pageable){
        return repo.findByNameContainingIgnoreCase(keyword,pageable);
    }
}
