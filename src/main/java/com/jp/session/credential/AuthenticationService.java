package com.jp.session.credential;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jp.session.entity.Role;
import com.jp.session.entity.User;
import com.jp.session.infra.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService implements UserDetailsService {

	private final UserRepository userRepository;

	public AuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info(email);
		User user = getByEmail(email);
		log.info(user.getEmail(), user.getPassword());
		return UserInfo.of(user, getGrantedAuthority(user.getRole()));
	}

	private List<GrantedAuthority> getGrantedAuthority(Role role) {
		return List.of(new SimpleGrantedAuthority(role.getUserRole().getName()));
	}

	public User getByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 계정이 없습니다."));
	}
}
