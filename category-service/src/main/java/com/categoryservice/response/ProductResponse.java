package com.categoryservice.response;

import java.util.List;

import org.apache.http.Header;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}