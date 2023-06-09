package com.jp.session.oauth;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jp.session.constant.UserRole;
import com.jp.session.credential.UserInfo;
import com.jp.session.entity.Role;
import com.jp.session.entity.User;
import com.jp.session.infra.RoleRepository;
import com.jp.session.infra.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final DefaultOAuth2UserService defaultOAuth2UserService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public CustomOAuthUserService(DefaultOAuth2UserService defaultOAuth2UserService, UserRepository userRepository,
		RoleRepository roleRepository) {
		this.defaultOAuth2UserService = defaultOAuth2UserService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());

		// 권한은 DB 에서 따로 가져온다.
		UserRole role = getUser(oAuth2Attribute).getRole().getUserRole();
		UserInfo of = UserInfo.of(oAuth2Attribute, List.of(new SimpleGrantedAuthority(role.getName())));
		// log.info(of.getAuthorities().toString());

		return of;
	}

	private User getUser(OAuth2Attribute oAuth2Attribute) {
		return userRepository.findByEmail(oAuth2Attribute.getEmail())
			.orElseGet(() -> signUp(oAuth2Attribute));
	}

	private User signUp(OAuth2Attribute oAuth2Attribute) {
		return userRepository.save(
			User.createUser(
				oAuth2Attribute.getEmail(),
				null,
				oAuth2Attribute.getName(),
				getRole()
			)
		);
	}

	private Role getRole() {
		return roleRepository.findByName(UserRole.USER).orElseGet(()
			-> roleRepository.save(new Role(UserRole.USER)));
	}
}
