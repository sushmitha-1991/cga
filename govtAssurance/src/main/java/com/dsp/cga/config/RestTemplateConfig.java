package com.dsp.cga.config;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .additionalInterceptors(jwtInterceptor())
                .build();
    }

    private ClientHttpRequestInterceptor jwtInterceptor() {
        return (req, body, execution) -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs != null) {
                HttpServletRequest currentRequest = attrs.getRequest();
                String jwtToken = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);

                if (jwtToken != null && !jwtToken.isEmpty()) {
                    req.getHeaders().set(HttpHeaders.AUTHORIZATION, jwtToken);
                }
            }
            return execution.execute(req, body);
        };
    }
    
//	@Bean
//	public RestTemplate restTemplate() {
//		return new RestTemplate();
//	}
	
//    private static final Logger log = LoggerFactory.getLogger(RestTemplateConfig.class);
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        log.debug("Creating RestTemplate with JWT interceptor");
//
//        return builder
//                .additionalInterceptors(jwtInterceptor())
//                .setConnectTimeout(Duration.ofSeconds(10))
//                .setReadTimeout(Duration.ofSeconds(10))
//                .build();
//    }
//
//    private ClientHttpRequestInterceptor jwtInterceptor() {
//        return (req, body, execution) -> {
//            ServletRequestAttributes attrs =
//                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//
//            if (attrs != null) {
//                HttpServletRequest currentRequest = attrs.getRequest();
//                String jwtToken = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
//
//                if (jwtToken != null && !jwtToken.isEmpty()) {
//
//                	URI targetUri = req.getURI();
//                    String currentHost = currentRequest.getServerName();
//                    int currentPort = currentRequest.getServerPort();
//
//                    // Prevent recursion if request is to the *same endpoint* being executed
//                    boolean sameHost = targetUri.getHost().equalsIgnoreCase(currentHost);
//                    boolean samePort = (targetUri.getPort() == -1 || targetUri.getPort() == currentPort);
//
//                    if (sameHost && samePort &&
//                        targetUri.getPath().equals(currentRequest.getRequestURI())) {
//                        log.warn("Skipping JWT propagation to avoid recursive self-call: {}", targetUri);
//                    } else {
//                        log.debug("Attaching JWT to outgoing request (starts with: {})",
//                                jwtToken.substring(0, Math.min(10, jwtToken.length())));
//                        req.getHeaders().set(HttpHeaders.AUTHORIZATION, jwtToken);
//                    }
//                } else {
//                    log.warn("No JWT token found in current request headers");
//                }
//            }
//
//            return execution.execute(req, body);
//        };
//    }
}


