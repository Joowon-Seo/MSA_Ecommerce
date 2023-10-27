package com.example.userservice.security;

import com.example.userservice.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationManager implements AuthenticationManager {

	private final CustomUserDetailService customUserDetailService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException{
		UserDetails userDetails = customUserDetailService
				.loadUserByUsername(authentication.getName());
		if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			throw new BadCredentialsException("Wrong password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
	}
}
