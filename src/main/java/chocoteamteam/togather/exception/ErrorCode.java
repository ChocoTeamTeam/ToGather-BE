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
    MEMBER_STATUS_WAIT(HttpStatus.BAD_REQUEST, "대기중인 회원입니다."),
    MEMBER_STATUS_BANNED(HttpStatus.BAD_REQUEST, "정지된 회원입니다."),
    MEMBER_STATUS_WITHDRAWAL(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),
    ABNORMAL_ACCESS(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다."),


    EXIST_TRUE_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    MISS_MATCH_PROVIDER(HttpStatus.BAD_REQUEST, "다른 SNS 계정으로 회원가입이 돼있습니다."),
    NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다."),
    NOT_FOUND_PROJECT(HttpStatus.BAD_REQUEST, "프로젝트를 찾을 수 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 인증 토큰 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}