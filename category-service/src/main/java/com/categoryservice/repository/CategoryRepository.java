package com.categoryservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.categoryservice.entity.Category;
import com.categoryservice.response.CategoryResponse;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
	
	CategoryResponse findByNameAllIgnoreCase(String category); 

}
