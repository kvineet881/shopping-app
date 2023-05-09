package com.categoryservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document(collection = "categories")
public class Category {
	@Id
    private String id;
    private String name;
  
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    // getters and setters omitted for brevity
    
}