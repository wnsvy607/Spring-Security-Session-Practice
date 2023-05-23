package com.jp.session.setup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jp.session.constant.UserRole;
import com.jp.session.entity.Role;
import com.jp.session.entity.User;
import com.jp.session.infra.RoleRepository;
import com.jp.session.infra.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	boolean alreadySetup = false;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;

	public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;
		// 롤저장
		Role userRole = getRole(UserRole.USER);
		Role adminRole = getRole(UserRole.ADMIN);

		String encodedPwd = encoder.encode("123");

		User user = User.createUser("user@abc.com", encodedPwd, "일반 유저", userRole);
		User admin = User.createUser("admin@abc.com", encodedPwd, "관리자 유저", adminRole);

		userRepository.save(user);
		userRepository.save(admin);

		alreadySetup = true;
	}

	private Role getRole(UserRole userRole) {
		return roleRepository.findByName(userRole)
			.orElseGet(() -> roleRepository.save(new Role(userRole)));
	}
}
