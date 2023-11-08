package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.repository.OrderEntity;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

	private final OrderService orderService;
	private final Environment env;
	private final KafkaProducer kafkaProducer;
	private final OrderProducer orderProducer;

	@GetMapping("/health-check")
	public String status() {
		return String.format("It's Working in order Service on PORT %s",
				env.getProperty("local.server.port"));
	}

	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
													 @RequestBody RequestOrder order) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


		OrderDto orderDto = mapper.map(order, OrderDto.class);
		orderDto.setUserId(userId);

		/**
		 * jpa
		 */
//		OrderDto createdOrder = orderService.createOrder(orderDto);
//		ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

		/**
		 * Kafka
		 */
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(order.getUnitPrice() * order.getQuantity());


		/**
		 * send this order to the kafka
		 */
		ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

		kafkaProducer.send("example-product-topic", orderDto);
		orderProducer.send("orders", orderDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrdersByUserId(@PathVariable("userId") String userId) {
		Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

		List<ResponseOrder> result = new ArrayList<>();
		orderList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseOrder.class));
		});

		return ResponseEntity.ok(result);
	}
}
