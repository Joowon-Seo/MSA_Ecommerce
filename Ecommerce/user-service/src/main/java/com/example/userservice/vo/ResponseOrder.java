package com.example.userservice.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResponseOrder {
	private String productId;
	private Integer quantity;
	private Integer unitPrice;
	private Integer totalPrice;
	private LocalDateTime createdAt;

	private String orderId;
}
