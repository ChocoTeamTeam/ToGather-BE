package chocoteamteam.togather.config;

import chocoteamteam.togather.component.security.JwtAccessDeniedHandler;
import chocoteamteam.togather.component.security.JwtAuthenticationFilter;
import chocoteamteam.togather.component.security.JwtEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtEntryPoint jwtEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest().permitAll();

		http.headers().frameOptions().disable();

		http.cors().configurationSource(corsConfigurationSource())
			.and()
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.formLogin().disable();
		http.logout().disable();

		http.exceptionHandling()
			.authenticationEntryPoint(jwtEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler);

		http.addFilterBefore(jwtAuthenticationFilter,
			UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
