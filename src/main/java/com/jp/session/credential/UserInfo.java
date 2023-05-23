package com.jp.session.credential;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jp.session.entity.User;

import lombok.ToString;

@ToString
public class UserInfo implements UserDetails {

	private String email;
	private String password;
	private List<GrantedAuthority> authorities;

	private String nickname;

	public String getNickname() {
		return this.nickname;
	}

	public UserInfo(String email, String password, List<GrantedAuthority> authorities, String nickname) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.nickname = nickname;
	}

	public static UserInfo of(User user, List<GrantedAuthority> authorities) {
		return new UserInfo(user.getEmail(), user.getPassword(), authorities, user.getNickname());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
