package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException.FeignClientException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final RestTemplate restTemplate;
	private final Environment env;
	private final OrderServiceClient orderServiceClient;

	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());

		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

		userRepository.save(userEntity);

		return mapper.map(userEntity, UserDto.class);
	}

	// TODO: 2023-10-23 존재하지 않는 유저에 대한 예외처리 필요
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//		List<ResponseOrder> orders = new ArrayList<>();
		/**
		 * Using as RestTemplate
		 */
//		String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//		ResponseEntity<List<ResponseOrder>> orderListResponse =
//				restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//					new ParameterizedTypeReference<List<ResponseOrder>>() {
//				});
//		List<ResponseOrder> orderList = orderListResponse.getBody();

		/**
		 * Using a FeignClient
		 */
		/**
		 * Feign exception handling
		 */
//		List<ResponseOrder> orderList = null;
//		try {
//			orderList = orderServiceClient.getOrders(userId);
//		} catch (FeignClientException ex) {
//			log.error(ex.getMessage());
//		}

		List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
		userDto.setOrders(orderList);

		return userDto;
	}

	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}
}
