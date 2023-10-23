package com.example.productservice.controller;

import com.example.productservice.repository.ProductEntity;
import com.example.productservice.service.ProductService;
import com.example.productservice.vo.ResponseProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-service")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final Environment env;

	@GetMapping("/health-check")
	public String status() {
		return String.format("It's Working in product Service on PORT %s",
				env.getProperty("local.server.port"));
	}

	@GetMapping("/products")
	public ResponseEntity<List<ResponseProduct>> getProducts() {
		Iterable<ProductEntity> userList = productService.getAllProducts();

		List<ResponseProduct> result = new ArrayList<>();
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseProduct.class));
		});

		return ResponseEntity.ok(result);
	}
}
