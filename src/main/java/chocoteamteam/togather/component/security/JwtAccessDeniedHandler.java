package chocoteamteam.togather.component.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {

		setResponse(response);

		response.getWriter().write(objectMapper.writeValueAsString("권한이 없습니다."));
		response.getWriter().flush();
	}

	private void setResponse(HttpServletResponse response) {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
	}
}
