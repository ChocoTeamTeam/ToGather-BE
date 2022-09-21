package chocoteamteam.togather.component.security;

import static chocoteamteam.togather.component.jwt.JwtUtils.BEARER_PREFIX;
import static chocoteamteam.togather.exception.ErrorCode.*;

import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.exception.TokenException;
import chocoteamteam.togather.service.JwtService;
import chocoteamteam.togather.type.MemberStatus;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		authenticate(request, getJwtFrom(request));

		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request, String token) {
		if (StringUtils.hasText(token)) {
			try {

				LoginMember loginMember = LoginMember.from(jwtService.parseAccessToken(token));

				validateStatus(loginMember.getStatus());

				saveLoginMemberInSecurityContext(loginMember);

			} catch (Exception e) {
				SecurityContextHolder.clearContext();
				request.setAttribute("jwtError",e);
			}
		}
	}

	private String getJwtFrom(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}

		return null;
	}

	private void validateStatus(MemberStatus status) {
		switch (status) {
			case PERMITTED:
				return;
			case WAIT:
				throw new MemberException(MEMBER_STATUS_WAIT);
			case BANNED:
				throw new MemberException(MEMBER_STATUS_BANNED);
			case WITHDRAWAL:
				throw new MemberException(MEMBER_STATUS_WITHDRAWAL);
			default:
				throw new MemberException(ABNORMAL_ACCESS);
		}
	}

	private static void saveLoginMemberInSecurityContext(LoginMember loginMember) {
		JwtAuthenticationToken authentication = new JwtAuthenticationToken(
			loginMember, "",
			Arrays.asList(new SimpleGrantedAuthority(loginMember.getRole().name())));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


}
