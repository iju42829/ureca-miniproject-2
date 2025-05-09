package com.ureca.miniproject.config;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.user.dto.Role;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<User> optionalUser = userRepository.findByEmail(email);			
		
		if(optionalUser.isPresent()) {
			
			User user = optionalUser.get();
			Role userRole = user.getRole();
			
			  List<SimpleGrantedAuthority> authorities = List.of(
			            new SimpleGrantedAuthority("ROLE_" + userRole.name())
			        );

			//MyUserDetails 사용			
			UserDetails userDetails = MyUserDetails.builder()
					.username(user.getUserName())
					.password(user.getPassword())
					.email(user.getEmail())
					.authorities(authorities)
					.build();	
			return userDetails;
		}
		
		throw new UsernameNotFoundException(email);
		
	}
}
