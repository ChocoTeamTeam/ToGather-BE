package chocoteamteam.togather.testUtils;

import chocoteamteam.togather.component.security.JwtAuthenticationToken;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.Role;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithLoginMemberSecurityContextFactory implements WithSecurityContextFactory<WithLoginMember> {

	@Override
	public SecurityContext createSecurityContext(WithLoginMember annotation) {

		LoginMember member = LoginMember.builder()
			.id(annotation.id())
			.role(Role.valueOf(annotation.role()))
			.nickname(annotation.nickname())
			.status(MemberStatus.valueOf(annotation.status()))
			.build();

		JwtAuthenticationToken token = new JwtAuthenticationToken(
			member, "",
			Collections.singletonList(new SimpleGrantedAuthority(annotation.role())));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(token);

		return context;
	}
}
