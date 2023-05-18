package com.jp.session.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
