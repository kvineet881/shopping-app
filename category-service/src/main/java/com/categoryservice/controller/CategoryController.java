package com.categoryservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.categoryservice.response.Product;
import com.categoryservice.response.ResponseModal;
import com.categoryservice.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

/*1.The category page should not display products if their inventory is unavaila-ble or limited.
2.	If there are no products available with sufficient inventory, an error message should be displayed on the category page.
3.	If a non-existent category  is requested, an error message is to be sent back*/
@RestController
@RequestMapping("/categories")
@Tag(name = "category-service", description = "category-service APIs")
public class CategoryController {
	private Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
    
    @Autowired
    private CategoryService categoryService;
    
    @Operation(summary = "Retrieve a Product by category", description = "Get a Product object by specifying its category. The response is List of Product object with id, name,brand,description,price,inventary and attributes")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = Product.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "204",description = "Data Not Availabe", content = { @Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    
    @GetMapping("{categoryName}/products/")
    public ResponseEntity<?> getCategoryProducts(@PathVariable("categoryName")  String categoryName,
    		@RequestParam(value = "sortBy", defaultValue = "inventory") String sortBy,
    		@RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
    		@RequestParam(value = "pageNo", defaultValue = "1") @Min(1) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    	  logger.info("CategoryController  getCategoryProducts() method calling..");
    	  ResponseModal responseModal = categoryService.getCategoryProduct(categoryName, sortBy, sortOrder,pageNo, pageSize);
    	  return ResponseEntity.ok(responseModal);
    }
    
    

}