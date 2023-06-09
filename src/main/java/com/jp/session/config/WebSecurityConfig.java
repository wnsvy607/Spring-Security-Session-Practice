package com.jp.session.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jp.session.oauth.CustomOAuthUserService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class WebSecurityConfig {

	private final CustomOAuthUserService oAuthUserService;

	public WebSecurityConfig(CustomOAuthUserService oAuthUserService) {
		this.oAuthUserService = oAuthUserService;
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web -> web.ignoring().requestMatchers(PathRequest.toH2Console()));
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement()
			.maximumSessions(1)
			.maxSessionsPreventsLogin(false)
			.expiredUrl("/home?expired");

		http.authorizeHttpRequests(request ->
				request.requestMatchers("/", "/home")
				.permitAll()
				.anyRequest()
				.authenticated())
			.formLogin(form ->
				form.loginPage("/form")
					.permitAll()
					.loginProcessingUrl("/perform_login"))
			.logout(LogoutConfigurer::permitAll);

		http.oauth2Login()
			.userInfoEndpoint()
			.userService(oAuthUserService);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	static RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		// 다른 예시: "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER"
		String hierarchy = "ROLE_ADMIN > ROLE_USER\n" + "ROLE_USER > ROLE_GUEST";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}

	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}
}

