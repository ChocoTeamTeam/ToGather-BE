package chocoteamteam.togather.exception;

import chocoteamteam.togather.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectException.class)
    public ErrorResponse projectExceptionHandler(ProjectException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(MemberException.class)
    public ErrorResponse memberExceptionHandler(MemberException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(CustomOAuthException.class)
    public ErrorResponse customOAuthException(CustomOAuthException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

	  @ExceptionHandler(TokenException.class)
    public ErrorResponse tokenExceptionHandler(TokenException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(ProjectMemberException.class)
    public ErrorResponse projectMemberExceptionHandler(ProjectMemberException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(ChatRoomException.class)
    public ErrorResponse chatRoomExceptionHandler(ChatRoomException e) {
        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }
    
    @ExceptionHandler(TechStackException.class)
    public ErrorResponse techStackExceptionHandler(TechStackException e) {

        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(ApplicantException.class)
    public ErrorResponse applicantExceptionHandler(ApplicantException e) {

        log.error("{} is occured", e.getErrorCode());
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }



}