package com.jp.session.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jp.session.credential.UserInfo;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1")
public class TestController {

	@GetMapping("/test")
	public String hello(HttpSession session) {
		if (session != null) {
			if(session.getAttribute("name") != null)
				return (String) session.getAttribute("name");
			session.setAttribute("name", "hello");
			return session.getId();
		}
		return "new one";

	}



	@GetMapping("/token")
	public String token(UsernamePasswordAuthenticationToken token) {
		if(token.getPrincipal().getClass().isAssignableFrom(UserInfo.class)) {
			UserInfo userInfo = (UserInfo) token.getPrincipal();
			return userInfo.getNickname() + "\n"
				+ token.getDetails();
		}
		return "the token's principal is not the instance of UserInfo.class";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin(UsernamePasswordAuthenticationToken token) {
		if(token.getPrincipal().getClass().isAssignableFrom(UserInfo.class)) {
			UserInfo userInfo = (UserInfo) token.getPrincipal();
			return userInfo.getNickname() + "\n"
				+ token.getDetails();
		}
		return "the token's principal is not the instance of UserInfo.class";
	}


	@GetMapping("/info")
	public String userInfo(@AuthenticationPrincipal UserInfo info) {
		return info.toString();
	}


}
