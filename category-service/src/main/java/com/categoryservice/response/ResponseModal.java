package com.categoryservice.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModal {
	
	private CategoryResponse categoryResponse;
	private ProductResponse productResponse;
	public CategoryResponse getCategoryResponse() {
		return categoryResponse;
	}
	public void setCategoryResponse(CategoryResponse categoryResponse) {
		this.categoryResponse = categoryResponse;
	}
	public ProductResponse getProductResponse() {
		return productResponse;
	}
	public void setProductResponse(ProductResponse productResponse) {
		this.productResponse = productResponse;
	}
	
	
	

}
