package com.example.userservice.dto;

import com.example.userservice.vo.ResponseOrder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private String email;
	private String name;
	private String password;
	private String userId;
	private LocalDateTime createdAt;

	private String encryptedPassword;

	private List<ResponseOrder> orders;
}
