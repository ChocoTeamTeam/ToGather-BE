package chocoteamteam.togather.component.security;

import chocoteamteam.togather.dto.ErrorResponse;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.exception.TokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		setResponse(response);

		Object jwtError = request.getAttribute("jwtError");

		ErrorResponse errorResponse;
		if (jwtError instanceof TokenException|| jwtError instanceof MemberException) {
			errorResponse = ErrorResponse.builder()
				.status(((TokenException) jwtError).getStatus())
				.errorCode(((TokenException) jwtError).getErrorCode())
				.errorMessage(((TokenException) jwtError).getErrorMessage())
				.build();
			response.setStatus(errorResponse.getStatus());
		} else {
			errorResponse = ErrorResponse.builder()
				.status(ErrorCode.INVALID_TOKEN.getHttpStatus().value())
				.errorCode(ErrorCode.INVALID_TOKEN)
				.errorMessage(ErrorCode.INVALID_TOKEN.getErrorMessage())
				.build();
		}

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		response.getWriter().flush();

	}

	private void setResponse(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
	}
}
