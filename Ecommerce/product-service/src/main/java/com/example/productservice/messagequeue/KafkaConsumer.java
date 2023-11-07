package com.example.productservice.messagequeue;

import com.example.productservice.repository.ProductEntity;
import com.example.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

	private final ProductRepository productRepository;

	@KafkaListener(topics = "example-product-topic")
	public void updateQuantity(String kafkaMessage) {
		log.info("Kafka Message : {}", kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
		} catch (JsonProcessingException ex) {
			ex.printStackTrace();
		}

		ProductEntity entity = productRepository.findByProductId((String) map.get("productId"));
		if (entity != null) {
			entity.setStock(entity.getStock() - (Integer) map.get("quantity"));
			productRepository.save(entity);
		}
	}
}
