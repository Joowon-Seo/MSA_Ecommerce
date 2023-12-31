package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.CustomUserDetailService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final CustomUserDetailService customUserDetailService;
	private final CustomAuthenticationManager authenticationManager;
	private final Environment env;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException{

		try {
			RequestLogin cred = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							cred.getEmail(),
							cred.getPassword(),
							new ArrayList<>()
					)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException{
		String userName = authResult.getName();
		UserDto userDetails = customUserDetailService.getUserDetailsByEmail(userName);
		log.debug("userDetails : {}", userDetails);

		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
				.compact();

		log.info("token : {}", env.getProperty("token.secret"));

		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
	}
}
