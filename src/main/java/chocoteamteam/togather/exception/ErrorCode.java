package chocoteamteam.togather.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_MATCH_MEMBER_PROJECT(HttpStatus.BAD_REQUEST, "해당 게시글에 대한 권한이 없습니다"),
    NOT_FOUND_TECH_STACK(HttpStatus.BAD_REQUEST, "해당 기술 스택을 찾을 수 없습니다"),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다"),
    NOT_FOUND_PROJECT(HttpStatus.BAD_REQUEST, "프로젝트를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}