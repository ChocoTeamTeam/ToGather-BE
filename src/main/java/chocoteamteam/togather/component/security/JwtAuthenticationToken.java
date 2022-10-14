package chocoteamteam.togather.component.security;

import chocoteamteam.togather.dto.LoginMember;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private LoginMember principal;
	private Object credentials;

	public JwtAuthenticationToken(LoginMember principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public LoginMember getPrincipal() {
		return this.principal;
	}

}
