package com.example.userservice.security;

import com.example.userservice.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurity {

	private final CustomAuthenticationManager CustomAuthenticationManager;
	private final CustomUserDetailService customUserDetailService;
	private final Environment env;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf().disable();
//		http.authorizeHttpRequests().antMatchers("/users/**").permitAll();
		http.authorizeHttpRequests().antMatchers("/actuator/**").permitAll();
		http.authorizeHttpRequests().antMatchers("/**").permitAll()
				.and()
				.addFilter(getAuthenticationFilter());


		http.headers().frameOptions().disable(); // h2-console frame 꺠짐 현상
		return http.build();
	}

	@Bean
	public AuthenticationFilter getAuthenticationFilter() {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(customUserDetailService, CustomAuthenticationManager, env);
		authenticationFilter.setAuthenticationManager(CustomAuthenticationManager);

		return authenticationFilter;
	}

}