package chocoteamteam.togather.exception;

import chocoteamteam.togather.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectException.class)
    public ErrorResponse exceptionHandler(ProjectException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }
}