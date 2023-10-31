package com.example.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseOrder {
	private String productId;
	private Integer quantity;
	private Integer unitPrice;
	private Integer totalPrice;
	private LocalDateTime createdAt;

	private String orderId;
}
