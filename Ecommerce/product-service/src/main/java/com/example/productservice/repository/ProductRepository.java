package com.example.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	ProductEntity findByProductId(String productId);

}
