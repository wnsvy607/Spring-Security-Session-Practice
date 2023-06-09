package com.jp.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import com.jp.session.infra.RoleRepository;
import com.jp.session.infra.UserRepository;
import com.jp.session.oauth.CustomOAuthUserService;

@Configuration
public class OAuthConfig {

	@Bean
	public CustomOAuthUserService oAuthUserService(DefaultOAuth2UserService defaultOAuth2UserService,
		UserRepository userRepository, RoleRepository roleRepository) {
		return new CustomOAuthUserService(defaultOAuth2UserService, userRepository, roleRepository);
	}

	@Bean
	public DefaultOAuth2UserService defaultOAuth2UserService() {
		return new DefaultOAuth2UserService();
	}
}
