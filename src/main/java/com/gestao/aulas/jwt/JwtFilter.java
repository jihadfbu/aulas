package com.gestao.aulas.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserDetailsService userDetailsService;
	private JwtTokenUtil jwtTokenUtil;
	private String authToken = null;

	public JwtFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Optional<HttpServletRequest> optReq = Optional.of(request);

		authToken = optReq.map(req -> req.getHeader("Authorization")).filter(token -> !token.isEmpty())
				.map(token -> token.replace("Bearer ", "")).orElse(null);

		if (authToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userDetailsService
						.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(authToken));

				if (userDetails != null && jwtTokenUtil.isTokenValid(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (ExpiredJwtException exception) {
				
				logger.warn("Request to parse expired JWT : {} failed : {}", authToken, exception.getMessage());
			}
		} else {
			//response.sendError(403, "Token inexistente");
			logger.info("token not presented...");
		}

		chain.doFilter(request, response);
	}
}
