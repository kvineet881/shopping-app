package com.categoryservice.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.categoryservice.response.ResponseModal;
import com.categoryservice.service.CategoryService;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategoryProducts_WithValidInputs_ReturnsResponseEntityWithOKStatus() {
        // Arrange
        String categoryName = "electronics";
        String sortBy = "price";
        String sortOrder = "desc";
        int pageNo = 1;
        int pageSize = 10;
        ResponseModal responseModal = new ResponseModal();

        when(categoryService.getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize))
                .thenReturn(responseModal);

        // Act
        ResponseEntity<?> responseEntity = categoryController.getCategoryProducts(categoryName, sortBy, sortOrder,
                pageNo, pageSize);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseModal, responseEntity.getBody());

        verify(categoryService, times(1))
                .getCategoryProduct(categoryName, sortBy, sortOrder, pageNo, pageSize);
        verifyNoMoreInteractions(categoryService);
    }


}
