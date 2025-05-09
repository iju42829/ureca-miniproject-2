package com.ureca.miniproject.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
		response.setContentType("application/json");
		String jsonStr = """
				{"result" : "fail"}
				""";
		response.getWriter().write(jsonStr);
	}

}
