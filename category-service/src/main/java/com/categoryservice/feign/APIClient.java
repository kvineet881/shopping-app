package com.categoryservice.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.categoryservice.response.Product;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

//@FeignClient(name="currency-exchange", url="localhost:8000")
@FeignClient(name="online-shopping-app")
//@FeignClient(value = "ONLINE-SHOPPING-APP", url = "http://localhost:8080")
public interface APIClient {
	@GetMapping("/products/getProductByCategory/{category}")
	@Retry(name = "online-shopping-app",fallbackMethod = "getProductFallback")
	//@CircuitBreaker(name = "online-shopping-app", fallbackMethod = "getAlbumsFallback")
	//@RateLimiter(name="default")
	//@Bulkhead(name="sample-api")
	public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String category) throws Exception;

	
	default String getProductFallback(String id, Throwable exception) {
		System.out.println("Param = " + id);
		System.out.println("Exception class=" + exception.getClass().getName());
		System.out.println("Exception took place: " + exception.getMessage());
		return "product service is down, please try after some time !";
		
	}
}
