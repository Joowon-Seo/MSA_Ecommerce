package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.Field;
import com.example.orderservice.dto.KafkaOrderDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.Payload;
import com.example.orderservice.dto.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	List<Field> fields = Arrays.asList(
			new Field("string", true, "order_id"),
			new Field("string", true, "user_id"),
			new Field("string", true, "product_id"),
			new Field("int32", true, "quantity"),
			new Field("int32", true, "unit_price"),
			new Field("int32", true, "total_price")
	);

	Schema schema = Schema.builder()
			.type("struct")
			.fields(fields)
			.optional(false)
			.name("orders")
			.build();

	public OrderDto send(String topic, OrderDto orderDto) {
		Payload payload = Payload.builder()
				.order_id(orderDto.getOrderId())
				.user_id(orderDto.getUserId())
				.product_id(orderDto.getProductId())
				.quantity(orderDto.getQuantity())
				.unit_price(orderDto.getUnitPrice())
				.total_price(orderDto.getTotalPrice())
				.build();

		KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaOrderDto);
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Order Producer sent data from the order microservice : {}", kafkaOrderDto);

		return orderDto;
	}
}
