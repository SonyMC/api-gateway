package com.mailsonymathew.microservices.apigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@Component
public class LoggingFilter implements GlobalFilter {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		// Log Request Path
		logger.info("Path of the request received -> {}",exchange.getRequest().getPath());
		
		//Implemt any authentication filterign you want to do here
		
		
		// Let the execution continue...
		return chain.filter(exchange);
	}

}
