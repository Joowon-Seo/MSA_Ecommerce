package com.example.orderservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrder {
	private String productId;
	private Integer quantity;
	private Integer unitPrice;
}
