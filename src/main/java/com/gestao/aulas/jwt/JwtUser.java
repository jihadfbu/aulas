package com.gestao.aulas.jwt;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final Integer id;
	private final String username;
	private final String token;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;
	private final boolean enabled;
	//TODO
	//private final Date lastPasswordResetDate;

	public JwtUser(Integer id, String username, String token, String password,
			Collection<? extends GrantedAuthority> authorities/*, boolean enabled*/) {
		this.id = id;
		this.username = username;
		this.token = token;
		this.password = password;
		this.authorities = authorities;
		this.enabled = true;
		//this.lastPasswordResetDate = lastPasswordResetDate;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
