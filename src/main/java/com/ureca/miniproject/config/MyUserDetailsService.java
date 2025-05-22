package com.ureca.miniproject.config;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	//UserDetailsService에서 제공하는 method가 loadUSerByUsername 밖에 없음 -> 실제 값은 email을 넘겨주는 방식으로 custom
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<User> optionalUser = userRepository.findByEmail(email);			
		
		if(optionalUser.isPresent()) {
			
			User user = optionalUser.get();
			//공통코드 적용
//			Role userRole = user.getRole();			
//			  List<SimpleGrantedAuthority> authorities = List.of(
//			            new SimpleGrantedAuthority("ROLE_" + userRole.name())
//			        );
			String userRole = user.getRole();
			List<SimpleGrantedAuthority> authorities = List.of(
				  new SimpleGrantedAuthority("ROLE_" + userRole)
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
