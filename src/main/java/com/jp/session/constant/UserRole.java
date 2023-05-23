package com.jp.session.constant;

/**
 * 상수처럼 쓰기위한 목적
 */
public enum UserRole {

	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	UNAUTH("ROLE_UNAUTH");

	private final String name;

	UserRole(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
