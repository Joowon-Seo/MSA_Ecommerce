package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUser {

	@NotNull(message = "Email can't be null")
	@Size(min = 2, message = "Email not be less than two characters")
	@Email
	private String email;

	@NotNull(message = "Name can't be null")
	@Size(min = 2, message = "Name not be less than two characters")
	private String name;

	@NotNull(message = "Name can't be null")
	@Size(min = 8, message = "password must be equal or grater than 8 characters")
	private String password;

}
