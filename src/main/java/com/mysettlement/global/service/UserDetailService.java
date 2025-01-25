package com.mysettlement.global.service;

import com.mysettlement.domain.user.repository.UserRepository;
import com.mysettlement.global.jwt.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return new UserDetail(userRepository
				                      .findByEmail(email)
				                      .orElseThrow(() -> new UsernameNotFoundException("No user has found")));
	}
}
