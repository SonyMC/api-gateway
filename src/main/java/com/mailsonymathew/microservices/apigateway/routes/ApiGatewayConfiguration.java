// Use this class to customize routes

package com.mailsonymathew.microservices.apigateway.routes;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ApiGatewayConfiguration {
	
	@Autowired
	RouteDefinitionLocator locator;
	
	

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		
		//return builder.routes().build(); //Use if we are not customizing any outes
		return builder.routes()
				.route(p -> p
						.path("/get")
						.filters(f -> f
								.addRequestHeader("MyHeader", "MyURI")   // add custom header value for all get 
								.addRequestParameter("Param", "MyValue"))  // add custom parameter for all get
						.uri("http://httpbin.org:80"))
				.route(p -> p
						 .path("/currency-exchange/**")  // redirect to 'currency-exchange-service-v2' registered in eureka  and replace the currency-exchange part in url with the uri below
						.uri("lb://currency-exchange-service-v2"))
				.route(p -> p
						 .path("/currency-conversion/**") // redirect to 'currency-conversion-service-v2' registered in eureka
						.uri("lb://currency-conversion-service-v2"))
				.route(p -> p
						.path("/currency-converter-feign/**") //redirect to 'currency-conversion-service-v2' registered in eureka
						.uri("lb://currency-conversion-service-v2"))
				.route(p -> p
						.path("/currency-conversion-new/**")
						.filters(f -> f.rewritePath(  // redirect to feign url and rewrite path
								"/currency-conversion-new/(?<segment>.*)",  // what is to be replaced 
								"/currency-converter-feign/${segment}"))  // what to replace with. Also append whatever is in teh segment after
						.uri("lb://currency-conversion-service-v2"))
			
				.build();
			}

/** Note : The below code is for generating API documentation for all services ruted through the AGteway. Unfortuntaly it does not work!!! **/ 
//			@Bean
//			public List<GroupedOpenApi> apis() {
//				   List<GroupedOpenApi> groups = new ArrayList<>();
//				   List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//				   definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//				      String name = routeDefinition.getId().replaceAll("-service", "");
//				      GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
//				   });
//				   return groups;
//			}
//			
//		    @Bean
//		    public OpenAPI customOpenAPI() {
//		        return new OpenAPI().components(new Components()).info(new Info().title("Spring MVC REST API")
//		                .contact(new Contact().name("Sony Mathew")).version("1.0.0"));
//		    }	
//			

}
