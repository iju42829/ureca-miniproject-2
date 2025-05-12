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
			 .authorizeHttpRequests(
					 authz -> {
					 authz.requestMatchers(
					    "/",
//					    "/chat/**",
						"/index.html",
					    "/ws/**",
					    "/topic/**",
					    "/app/**",
					    "/register.html",
					    "/api/auth/signup",
						"/signup.html",
					    "/login",
					    "/logout",				    
						"/csrf-token"												
					).permitAll()					 
				 .anyRequest().authenticated();
				 }
	        )
			 .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) //cookie로 csrf token를 넣어준다.(front,back 둘다에게?) 그 다음 부터는 이제 프론트에서 백엔드쪽으로 계쏙 token을 내려줘야 로그인 가능하다., cookie 기반으로 만들라고 요청을 하고, 				
			 .formLogin(form -> 
				form					
					.loginPage("/login.html")
					.loginProcessingUrl("/login")  
					.successHandler(successHandler)
					.failureHandler(failureHandler)
					.permitAll()
					)
			.logout(logout->logout.permitAll())								
			.build(); 	        
							
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
