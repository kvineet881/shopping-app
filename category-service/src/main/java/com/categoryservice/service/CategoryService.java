package com.categoryservice.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.categoryservice.exception.ProductNotAvailableException;
import com.categoryservice.exception.ResourceNotFoundException;
import com.categoryservice.feign.APIClient;
import com.categoryservice.repository.CategoryRepository;
import com.categoryservice.response.CategoryResponse;
import com.categoryservice.response.Product;
import com.categoryservice.response.ProductResponse;
import com.categoryservice.response.ResponseModal;

import jakarta.validation.constraints.Min;

@Service
public class CategoryService {
	private Logger logger = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private APIClient apiClient;

	public ResponseModal getCategoryProduct(String categoryName, String sortBy, String sortOrder, @Min(1) int pageNo,
			int pageSize) {
		  logger.info("CategoryService  getCategoryProducts() method calling..");
		CategoryResponse categoryResponse = categoryRepository.findByNameAllIgnoreCase(categoryName);
		if (categoryResponse == null) {
			  logger.info("category " + categoryName + "  is not available in databse !");
			throw new ResourceNotFoundException("category " + categoryName + "  is not available in databse !");
		}

		ResponseModal responseModal = new ResponseModal();

//	        HttpHeaders headers = new HttpHeaders();
//	        headers.set("accept", "application/json");
//	        HttpEntity requestEntity = new HttpEntity<>(null, headers);
//	        ResponseEntity<List<Product>>  responseEntity = new RestTemplate().
//	        	
//	        		  exchange("http://localhost:8080/products/getProductByCategory/"+categoryName,
//	        		            HttpMethod.GET,
//	        		            requestEntity,
//	        		            new ParameterizedTypeReference<List<Product>>() {
//	        		            });
//	        List<Product> product = responseEntity.getBody();
//	        System.out.println(product);
//	        System.out.println(responseEntity.getStatusCode());
		// OR

		List<Product> product = null;
		try {
			logger.info("CategoryService  getProductByCategory() method calling using feign client");
			product = apiClient.getProductByCategory(categoryName).getBody();
		} catch (Exception e) {
			logger.info("Exception occure in getProductByCategory() method calling using feign client"+e.getMessage());
			e.printStackTrace();
		}
		if(product != null && !product.isEmpty()) {
			if (sortBy != null) {
				if ("inventory".equalsIgnoreCase(sortBy)) {
					logger.info("Sorting product based on inventary assending order ");
					product.sort((p1, p2) -> Integer.compare(p1.getInventory().getAvailable(),
							p2.getInventory().getAvailable()));
				} else if ("price".equalsIgnoreCase(sortBy)) {
					logger.info("Sorting product based on price assending order");
					product.sort((p1, p2) -> Double.compare(p1.getPrice().getAmount(), p2.getPrice().getAmount()));
				}
			}
			if (sortOrder != null && "desc".equalsIgnoreCase(sortOrder) && "inventory".equalsIgnoreCase(sortBy)) {
				logger.info("Sorting product based on inventary desending order ");
				product.sort((p1, p2) -> Integer.compare(p2.getInventory().getAvailable(),
						p1.getInventory().getAvailable()));
			}
			if (sortOrder != null && "desc".equalsIgnoreCase(sortOrder) && "price".equalsIgnoreCase(sortBy)) {
				logger.info("Sorting product based on price desending order ");
				product.sort((p1, p2) -> Double.compare(p2.getPrice().getAmount(), p1.getPrice().getAmount()));
			}
			List<Product> availableProducts = product.stream().filter(p -> p.getInventory().getAvailable() > 10)
					.collect(Collectors.toList());

			List<Product> availableProductsWithPagination = getObjectListWithPagination(availableProducts, pageNo,
					pageSize);
				
//			ModelMapper modelMapper = new ModelMapper();
//			ProductResponse productResponse = modelMapper.map(availableProductsWithPagination, ProductResponse.class);
			ProductResponse productResponse = mapToProduct(availableProductsWithPagination);
			categoryResponse.setCategoryName(categoryName);
			responseModal.setProductResponse(productResponse);
			responseModal.setCategoryResponse(categoryResponse);

		} else
		{
			logger.info("products-service is not available!");
			throw new ProductNotAvailableException("products-service is not available, please try after some time ! ");
		}

		return responseModal;
	}

	private ProductResponse mapToProduct(List<Product> products) {
		ProductResponse productResponse = new ProductResponse();
		productResponse.setProducts(products);
		return productResponse;
	}

	private List<Product> getObjectListWithPagination(List<Product> objectList, int page, int pageSize) {

		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, objectList.size());
		if (startIndex >= endIndex) {
			return Collections.emptyList();
		}
		return objectList.subList(startIndex, endIndex);
	}

}
