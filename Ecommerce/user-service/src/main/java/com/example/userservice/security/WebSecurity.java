package com.example.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurity {

	private final CustomAuthenticationManager CustomAuthenticationManager;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf().disable();
//		http.authorizeHttpRequests().antMatchers("/users/**").permitAll();
		http.authorizeHttpRequests().antMatchers("/**").permitAll()
				.and()
				.addFilter(getAuthenticationFilter());


		http.headers().frameOptions().disable(); // h2-console frame 꺠짐 현상
		return http.build();
	}

	@Bean
	public AuthenticationFilter getAuthenticationFilter() {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
		authenticationFilter.setAuthenticationManager(CustomAuthenticationManager);

		return authenticationFilter;
	}

}