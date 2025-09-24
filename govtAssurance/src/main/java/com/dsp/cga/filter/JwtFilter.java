/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.cga.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dsp.cga.assurance.service.impl.CustomUserDetailsService;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			final String authorizationHeader = request.getHeader("Authorization");

			String username = null;
			String jwt = null;

			log.debug("Authorization header: {}", authorizationHeader); // Debug log for Authorization header

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
				log.debug("JWT token found, username extracted: {}", username);
			} else {
				log.debug("No valid JWT token found in the Authorization header");
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				log.debug("Attempting to load user details for username: {}", username);
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				log.debug("UserDetails loaded for username: {}", username);

				if (userDetails.isEnabled() && jwtUtil.validateToken(jwt, userDetails)) {
					log.debug("JWT token validated successfully for user: {}", username);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					log.debug("User authenticated successfully: {}", username);
				} else {
					log.error("JWT token validation failed or user is not enabled for username: {}", username);
				}
			} else {
				log.debug("No username found or authentication already set in context");
			}
			chain.doFilter(request, response);
		}
		// changes for expired token

		catch (io.jsonwebtoken.ExpiredJwtException e) {
			log.error("Expired JWT token: {}", e.getMessage());
			sendErrorResponse(response, 498, false, "JWT token has expired");
		}
		// changes for expired token
		catch (JwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage(), e); // Log the exception stack trace
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid JWT token");
		} catch (Exception e) {
			log.error("Error during JWT processing: {}", e.getMessage(), e); // Log the exception stack trace
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("An error occurred while processing the authentication request");
		}
	}
	// changes for expired token

	private void sendErrorResponse(HttpServletResponse response, int status, boolean success, String message)

			throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		GenericResponse<Object> genericResponse = new GenericResponse<>(status, success, message, null);

		// Convert object to JSON
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(genericResponse);

		response.getWriter().write(json);
	}

	// changes for expired token
}
