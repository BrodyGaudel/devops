package com.mounanga.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

/**
 * The main entry point for the Gateway Service application.
 * This class initializes the Spring Boot application and configures the Spring Cloud Gateway.
 * It dynamically retrieves route information from registered service instances using the discovery service.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	/**
	 *This code configures a bean in Spring Cloud Gateway that uses the discovery service to
	 *  dynamically retrieve route information from registered service instances,
	 *  providing automatic route discovery in Spring Cloud-based infrastructure.
	 * @param reactiveDiscoveryClient it is an interface provided by Spring Cloud Netflix
	 *                                  to interact with the discovery service (e.g. Eureka, Consul, etc.)
	 *                                  in a reactive way.
	 * @param discoveryLocatorProperties it is a Spring Cloud Netflix configuration class that contains
	 *                                     properties related to service discovery.
	 * @return DiscoveryClientRouteDefinitionLocator it is a component provided
	 * by Spring Cloud Gateway that connects to the discovery service
	 * (such as Eureka) and automatically retrieves route information from registered
	 * service instances. It then generates the route definitions for
	 * the Spring Cloud Gateway router using this information.
	 */
	@Bean
	DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient reactiveDiscoveryClient,
																				DiscoveryLocatorProperties discoveryLocatorProperties){
		return new DiscoveryClientRouteDefinitionLocator(reactiveDiscoveryClient, discoveryLocatorProperties);
	}

}
