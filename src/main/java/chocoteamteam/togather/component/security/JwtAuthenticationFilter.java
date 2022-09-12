package chocoteamteam.togather.component.security;

import static chocoteamteam.togather.component.jwt.JwtUtils.BEARER_PREFIX;

import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.exception.MemberStatusException;
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
@Component
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

			} catch (RuntimeException e) {
				SecurityContextHolder.clearContext();

				request.setAttribute("errorMessage", e.getMessage());
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
				throw new MemberStatusException("대기중인 회원입니다.");
			case BANNED:
				throw new MemberStatusException("정지된 회원입니다.");
			case WITHDRAWAL:
				throw new MemberStatusException("탈퇴한 회원입니다.");
			default:
				throw new MemberStatusException("비정상적인 접근입니다.");
		}
	}

	private static void saveLoginMemberInSecurityContext(LoginMember loginMember) {
		JwtAuthenticationToken authentication = new JwtAuthenticationToken(
			loginMember, "",
			Arrays.asList(new SimpleGrantedAuthority(loginMember.getRole().name())));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


}
