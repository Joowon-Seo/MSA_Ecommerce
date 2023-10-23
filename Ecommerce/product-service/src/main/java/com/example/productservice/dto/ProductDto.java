package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductDto {
	private String productId;
	private Integer quantity;
	private Integer unitPrice;
	private Integer totalPrice;

	private String orderId;
	private String userId;
}
