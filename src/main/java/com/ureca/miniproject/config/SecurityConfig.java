package com.ureca.miniproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(
			HttpSecurity http,
			MyAuthenticationSuccessHandler successHandler,
			MyAuthenticationFailureHandler failureHandler			
			) throws Exception {
		return http			
			.headers(headers -> headers
	                .frameOptions(frameOptions -> frameOptions
	                    .sameOrigin() // 또는 .disable() 하면 완전히 끔
	                )
	            )
			 .authorizeHttpRequests(
					 authz -> {
					 authz.requestMatchers(
							 "/api/status/**",
					    "/",
					    "/chat/**",
						"/index.html",
					    "/ws/**",
					    "/topic/**",
					    "/app/**",
					    "/register.html",
					    "/api/auth/signup",
						"/signup.html",
					    "/login",
					    "/logout",				    
						"/csrf-token",	
					    "/my/success/endpoint",
						"/api/groupcodes/**",
						"/api/codes/**"
					).permitAll()					 
				 .anyRequest().authenticated();
				 }
	        )
			 .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) 				
			 .formLogin(form -> 
				form					
					.loginPage("/login.html")
					.loginProcessingUrl("/login")  
					.successHandler(successHandler)
					.failureHandler(failureHandler)
					.permitAll()
					)
			 
		    .logout((logout) -> 
		    	logout
		    		.logoutUrl("/my/logout")
		    		.logoutSuccessUrl("/my/success/endpoint")
		    		.permitAll()
		    		)
				
			.build(); 	        
							
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
