package com.jp.session.credential;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.jp.session.entity.User;
import com.jp.session.oauth.OAuth2Attribute;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.ToString;

@ToString
public class UserInfo implements UserDetails, OAuth2User {

	private String email;
	private String password;
	private String nickname;
	public boolean isOAuth = false;
	private Map<String, Object> attributes;
	private List<GrantedAuthority> authorities;




	private UserInfo(String email, String password, List<GrantedAuthority> authorities, String nickname) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.nickname = nickname;
		this.isOAuth = false;
		this.attributes = null;
	}

	@Builder(access = AccessLevel.PRIVATE)
	private UserInfo(String email, String nickname, boolean isOAuth, Map<String, Object> attributes,
		List<GrantedAuthority> authorities) {
		this.email = email;
		this.nickname = nickname;
		this.isOAuth = isOAuth;
		this.attributes = attributes;
		this.authorities = authorities;
		this.password = null;
	}

	public static UserInfo of (OAuth2Attribute oAuth2Attribute, List<GrantedAuthority> authorities) {
		return UserInfo.builder()
			.email(oAuth2Attribute.getEmail())
			.nickname(oAuth2Attribute.getName())
			.attributes(oAuth2Attribute.getAttributes())
			.authorities(authorities)
			.isOAuth(true)
			.build();
	}


	public static UserInfo of(User user, List<GrantedAuthority> authorities) {
		return new UserInfo(user.getEmail(), user.getPassword(), authorities, user.getNickname());
	}

	public String getNickname() {
		return this.nickname;
	}

	@Override
	public <A> A getAttribute(String name) {
		return OAuth2User.super.getAttribute(name);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.email;
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
