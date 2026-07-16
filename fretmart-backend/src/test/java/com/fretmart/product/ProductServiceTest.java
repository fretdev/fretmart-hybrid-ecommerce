package com.fretmart.product;

import com.fretmart.exception.DuplicateProductException;
import com.fretmart.exception.ProductNotFoundException;
import com.fretmart.product.dto.ProductRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductRequestDto sampleRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        sampleProduct = Product.builder()
                .id(1L)
                .name("Gaming Laptop")
                .description("High-performance gaming laptop")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .imageUrl("https://example.com/laptop.jpg")
                .category("Electronics")
                .build();

        sampleRequest = ProductRequestDto.builder()
                .name("Gaming Laptop")
                .description("High-performance gaming laptop")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .imageUrl("https://example.com/laptop.jpg")
                .category("Electronics")
                .build();
    }

    @Nested
    @DisplayName("Create Product Tests")
    class CreateProductTests {

        @Test
        @DisplayName("Should create product successfully when name does not exist")
        void createProduct_ShouldSaveAndReturnProduct_WhenValid() {
            when(productRepository.findByNameIgnoreCase("Gaming Laptop"))
                    .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

            Product result = productService.createProduct(sampleRequest);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Gaming Laptop");
            verify(productRepository, times(1)).findByNameIgnoreCase("Gaming Laptop");
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw DuplicateProductException when product name already exists")
        void createProduct_ShouldThrowException_WhenDuplicateName() {
            when(productRepository.findByNameIgnoreCase("Gaming Laptop"))
                    .thenReturn(Optional.of(sampleProduct));

            assertThatThrownBy(() -> productService.createProduct(sampleRequest))
                    .isInstanceOf(DuplicateProductException.class)
                    .hasMessage("Product already exists with name: Gaming Laptop");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should handle case-insensitive duplicate detection")
        void createProduct_ShouldDetectDuplicate_CaseInsensitive() {
            when(productRepository.findByNameIgnoreCase("gaming laptop"))
                    .thenReturn(Optional.of(sampleProduct));

            ProductRequestDto sameNameDifferentCase = ProductRequestDto.builder()
                    .name("gaming laptop")
                    .price(new BigDecimal("1299.99"))
                    .stock(10)
                    .build();

            assertThatThrownBy(() -> productService.createProduct(sameNameDifferentCase))
                    .isInstanceOf(DuplicateProductException.class)
                    .hasMessage("Product already exists with name: gaming laptop");
        }
    }

    @Nested
    @DisplayName("Get Product By ID Tests")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product when ID exists")
        void getProductById_ShouldReturnProduct_WhenIdExists() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

            Product result = productService.getProductById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Gaming Laptop");
            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when ID does not exist")
        void getProductById_ShouldThrowException_WhenIdNotFound() {
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(999L))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage("Product not found with id: 999");

            verify(productRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Update Product Tests")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update product successfully when name is unique")
        void updateProduct_ShouldUpdateAndReturnProduct_WhenValid() {
            ProductRequestDto updateRequest = ProductRequestDto.builder()
                    .name("Updated Laptop")
                    .description("Updated description")
                    .price(new BigDecimal("1499.99"))
                    .stock(5)
                    .imageUrl("https://example.com/updated.jpg")
                    .category("Electronics")
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
            when(productRepository.findByNameIgnoreCase("Updated Laptop"))
                    .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Product result = productService.updateProduct(1L, updateRequest);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Laptop");
            assertThat(result.getPrice()).isEqualTo(new BigDecimal("1499.99"));
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should allow update when keeping the same name (self match)")
        void updateProduct_ShouldAllowUpdate_WhenNameUnchanged() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
            when(productRepository.findByNameIgnoreCase("Gaming Laptop"))
                    .thenReturn(Optional.of(sampleProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Product result = productService.updateProduct(1L, sampleRequest);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Gaming Laptop");
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when updating non-existent product")
        void updateProduct_ShouldThrowException_WhenProductNotFound() {
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(999L, sampleRequest))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage("Product not found with id: 999");

            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw DuplicateProductException when updating to existing name (different product)")
        void updateProduct_ShouldThrowException_WhenUpdatingToDuplicateName() {
            Product existingDifferentProduct = Product.builder()
                    .id(2L)
                    .name("Existing Laptop")
                    .price(new BigDecimal("999.99"))
                    .stock(10)
                    .build();

            ProductRequestDto updateRequest = ProductRequestDto.builder()
                    .name("Existing Laptop")
                    .price(new BigDecimal("1499.99"))
                    .stock(5)
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
            when(productRepository.findByNameIgnoreCase("Existing Laptop"))
                    .thenReturn(Optional.of(existingDifferentProduct));

            assertThatThrownBy(() -> productService.updateProduct(1L, updateRequest))
                    .isInstanceOf(DuplicateProductException.class)
                    .hasMessage("Product already exists with name: Existing Laptop");
        }
    }

    @Nested
    @DisplayName("Delete Product Tests")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product successfully when ID exists")
        void deleteProduct_ShouldDelete_WhenIdExists() {
            when(productRepository.existsById(1L)).thenReturn(true);

            productService.deleteProduct(1L);

            verify(productRepository, times(1)).deleteById(1L);
            verify(productRepository,times(1)).existsById(1L);
        }

        @Test
        @DisplayName("Should not throw exception when deleting non-existent product")
        void deleteProduct_ShouldNotThrowException_WhenProductNotFound() {
            when(productRepository.existsById(999L)).thenReturn(false);

            assertThatThrownBy(()->productService.deleteProduct(999L)).isInstanceOf(ProductNotFoundException.class).hasMessage("Product not found with id: 999");

            verify(productRepository,times(1)).existsById(999L);
            verify(productRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Get All Products Tests")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return page of products when products exist")
        void getAllProducts_ShouldReturnPage_WhenProductsExist() {
            Page<Product> expectedPage = new PageImpl<>(List.of(sampleProduct), pageable, 1);
            when(productRepository.findAll(pageable)).thenReturn(expectedPage);

            Page<Product> result = productService.getAllProducts(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Gaming Laptop");
            verify(productRepository, times(1)).findAll(pageable);
        }

        @Test
        @DisplayName("Should return empty page when no products exist")
        void getAllProducts_ShouldReturnEmptyPage_WhenNoProductsExist() {
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            when(productRepository.findAll(pageable)).thenReturn(emptyPage);

            Page<Product> result = productService.getAllProducts(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, times(1)).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("Get Products By Category Tests")
    class GetProductsByCategoryTests {

        @Test
        @DisplayName("Should return filtered products when category exists")
        void getProductsByCategory_ShouldReturnFilteredProducts_WhenCategoryExists() {
            Page<Product> expectedPage = new PageImpl<>(List.of(sampleProduct), pageable, 1);
            when(productRepository.findByCategoryIgnoreCase("Electronics", pageable)).thenReturn(expectedPage);

            Page<Product> result = productService.getProductsByCategory("Electronics", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository, times(1)).findByCategoryIgnoreCase("Electronics", pageable);
        }

        @Test
        @DisplayName("Should return empty page when category has no products")
        void getProductsByCategory_ShouldReturnEmptyPage_WhenNoProductsInCategory() {
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            when(productRepository.findByCategoryIgnoreCase("Books", pageable)).thenReturn(emptyPage);

            Page<Product> result = productService.getProductsByCategory("Books", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, times(1)).findByCategoryIgnoreCase("Books", pageable);
        }

        @Test
        @DisplayName("Should return empty page when category is null")
        void getProductsByCategory_ShouldReturnEmptyPage_WhenCategoryIsNull() {
            Page<Product> result = productService.getProductsByCategory(null, pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, never()).findByCategoryIgnoreCase(any(), any());
        }

        @Test
        @DisplayName("Should return empty page when category is empty string")
        void getProductsByCategory_ShouldReturnEmptyPage_WhenCategoryIsEmpty() {
            Page<Product> result = productService.getProductsByCategory("", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, never()).findByCategoryIgnoreCase(any(), any());
        }
    }

    @Nested
    @DisplayName("Search Products Tests")
    class SearchProductsTests {

        @Test
        @DisplayName("Should return matching products when keyword exists")
        void searchProducts_ShouldReturnMatchingProducts_WhenKeywordMatches() {
            Page<Product> expectedPage = new PageImpl<>(List.of(sampleProduct), pageable, 1);
            when(productRepository.findByNameContainingIgnoreCase("laptop", pageable))
                    .thenReturn(expectedPage);

            Page<Product> result = productService.searchProducts("laptop", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository, times(1)).findByNameContainingIgnoreCase("laptop", pageable);
        }

        @Test
        @DisplayName("Should return empty page when keyword matches nothing")
        void searchProducts_ShouldReturnEmptyPage_WhenNoMatches() {
            Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            when(productRepository.findByNameContainingIgnoreCase("nonexistent", pageable))
                    .thenReturn(emptyPage);

            Page<Product> result = productService.searchProducts("nonexistent", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, times(1)).findByNameContainingIgnoreCase("nonexistent", pageable);
        }

        @Test
        @DisplayName("Should return empty page when keyword is null")
        void searchProducts_ShouldReturnEmptyPage_WhenKeywordIsNull() {
            Page<Product> result = productService.searchProducts(null, pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, never()).findByNameContainingIgnoreCase(any(), any());
        }

        @Test
        @DisplayName("Should return empty page when keyword is empty string")
        void searchProducts_ShouldReturnEmptyPage_WhenKeywordIsEmpty() {
            Page<Product> result = productService.searchProducts("", pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(productRepository, never()).findByNameContainingIgnoreCase(any(), any());
        }
    }
}