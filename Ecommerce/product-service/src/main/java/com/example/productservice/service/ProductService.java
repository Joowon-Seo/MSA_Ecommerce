package com.example.productservice.service;

import com.example.productservice.repository.ProductEntity;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public Iterable<ProductEntity> getAllProducts() {
		return productRepository.findAll();
	}
}
