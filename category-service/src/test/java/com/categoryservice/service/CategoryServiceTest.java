package com.categoryservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.categoryservice.exception.ProductNotAvailableException;
import com.categoryservice.exception.ResourceNotFoundException;
import com.categoryservice.feign.APIClient;
import com.categoryservice.repository.CategoryRepository;
import com.categoryservice.response.Attributes;
import com.categoryservice.response.Category;
import com.categoryservice.response.CategoryResponse;
import com.categoryservice.response.Inventory;
import com.categoryservice.response.Price;
import com.categoryservice.response.Product;
import com.categoryservice.response.ResponseModal;
import com.categoryservice.service.CategoryService;

import nonapi.io.github.classgraph.utils.Assert;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@Mock
    private CategoryRepository categoryRepository;

    @Mock
    private APIClient apiClient;

    @InjectMocks
    private CategoryService categoryService;

    private Product product;
	private Price price;

	private Inventory inventory;
	private List<Attributes> attributesList;
	private Attributes attributes;
	private Category category;
	@BeforeEach
	public void setup() {
		price = new Price();
		price.setAmount(49.99);
		price.setCurrency("USD");

		inventory = new Inventory();
		inventory.setAvailable(40);
		inventory.setReserved(10);
		inventory.setTotal(50);

		attributes = new Attributes();
		attributes.setName("Color");
		attributes.setValue("blue");
		attributesList = Arrays.asList(attributes);

		category = new Category();
		category.setName("Shirt");
		

		product = new Product("644a7e96590bb9082f919423", "Men's Striped Dress Shirt", "Brand Name",
				"Classic striped dress shirt for formal occasions.", price, category, inventory, attributesList);
		
	}
	

    @Test
    public void testGetCategoryProduct_ValidCategory_ReturnsResponseModal() throws Exception {
        // Arrange
        String categoryName = "shirt";
        String sortBy = "inventory";
        String sortOrder = "desc";
        int pageNo = 1;
        int pageSize = 10;

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryName(categoryName);

        Product product1 = new Product("644a7e96590bb9082f919423", "Men's Striped Dress Shirt", "Brand Name",
				"Classic striped dress shirt for formal occasions.", price, category, inventory, attributesList);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);

        when(categoryRepository.findByNameAllIgnoreCase(categoryName)).thenReturn(categoryResponse);
        when(apiClient.getProductByCategory(categoryName)).thenReturn(ResponseEntity.ok(products));

        // Act
        ResponseModal responseModal = categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);

        // Assert
        Assertions.assertNotNull(responseModal);
        Assertions.assertEquals(categoryName, responseModal.getCategoryResponse().getCategoryName());
        Assertions.assertEquals(2, responseModal.getProductResponse().getProducts().size());
        Assertions.assertEquals("Men's Striped Dress Shirt", responseModal.getProductResponse().getProducts().get(0).getName());
        Assertions.assertEquals("Men's Striped Dress Shirt", responseModal.getProductResponse().getProducts().get(1).getName());

        verify(categoryRepository, times(1)).findByNameAllIgnoreCase(categoryName);
        verify(apiClient, times(1)).getProductByCategory(categoryName);
    }
    
    @Test
    public void testGetCategoryProductSort_ValidCategory_ReturnsResponseModal() throws Exception {
        // Arrange
        String categoryName = "shirt";
        String sortBy = "price";
        String sortOrder = "desc";
        int pageNo = 1;
        int pageSize = 10;

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryName(categoryName);

        Product product1 = new Product("644a7e96590bb9082f919423", "Men's Striped Dress Shirt", "Brand Name",
				"Classic striped dress shirt for formal occasions.", price, category, inventory, attributesList);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);

        when(categoryRepository.findByNameAllIgnoreCase(categoryName)).thenReturn(categoryResponse);
        when(apiClient.getProductByCategory(categoryName)).thenReturn(ResponseEntity.ok(products));

        // Act
        ResponseModal responseModal = categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);

        // Assert
        Assertions.assertNotNull(responseModal);
        Assertions.assertEquals(categoryName, responseModal.getCategoryResponse().getCategoryName());
        Assertions.assertEquals(2, responseModal.getProductResponse().getProducts().size());
        Assertions.assertEquals("Men's Striped Dress Shirt", responseModal.getProductResponse().getProducts().get(0).getName());
        Assertions.assertEquals("Men's Striped Dress Shirt", responseModal.getProductResponse().getProducts().get(1).getName());

        verify(categoryRepository, times(1)).findByNameAllIgnoreCase(categoryName);
        verify(apiClient, times(1)).getProductByCategory(categoryName);
    }

    @Test
    public void testGetCategoryProduct_InvalidCategory_ThrowsResourceNotFoundException() throws Exception {
        // Arrange
        String categoryName = "Nonexistent Category";
        String sortBy = "inventory";
        String sortOrder = "asc";
        int pageNo = 1;
        int pageSize = 10;

        when(categoryRepository.findByNameAllIgnoreCase(categoryName)).thenReturn(null);

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);
        });

        verify(categoryRepository, times(1)).findByNameAllIgnoreCase(categoryName);
        verify(apiClient, never()).getProductByCategory(anyString());
    }

    @Test
    public void testGetCategoryProduct_NoProductsAvailable_ThrowsProductNotAvailableException() throws Exception {
        // Arrange
        String categoryName = "Test Category";
        String sortBy = "price";
        String sortOrder = "asc";
        int pageNo = 1;
        int pageSize = 10;

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryName(categoryName);

        when(categoryRepository.findByNameAllIgnoreCase(categoryName)).thenReturn(categoryResponse);
        when(apiClient.getProductByCategory(categoryName)).thenReturn(ResponseEntity.ok(List.of()));

        // Act & Assert
        Assertions.assertThrows(ProductNotAvailableException.class, () -> {
            categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);
        });

        verify(categoryRepository, times(1)).findByNameAllIgnoreCase(categoryName);
        verify(apiClient, times(1)).getProductByCategory(categoryName);
    }

    @Test
    public void testGetCategoryProduct_NoProductsAvailable() throws Exception {
        // Arrange
        String categoryName = "Category";
        String sortBy = "inventory";
        String sortOrder = "asc";
        int pageNo = 1;
        int pageSize = 10;

        assertThrows(ResourceNotFoundException.class, () -> {
        	 categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);
		});

    }
    
}

