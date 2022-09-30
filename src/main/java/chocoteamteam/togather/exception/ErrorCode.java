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
    MEMBER_STATUS_WAIT(HttpStatus.UNAUTHORIZED, "대기중인 회원입니다."),
    MEMBER_STATUS_BANNED(HttpStatus.UNAUTHORIZED, "정지된 회원입니다."),
    MEMBER_STATUS_WITHDRAWAL(HttpStatus.UNAUTHORIZED, "탈퇴한 회원입니다."),
    ABNORMAL_ACCESS(HttpStatus.UNAUTHORIZED, "비정상적인 접근입니다."),
    MAXIMUM_PROJECT_INTEREST(HttpStatus.BAD_REQUEST, "더 이상 관심 프로젝트를 추가할 수 없습니다."),
    EXIST_FALSE_GITHUB_EMAIL(HttpStatus.BAD_REQUEST,"Github의 공개된 이메일이 존재하지 않습니다."),
    MISS_MATCH_MEMBER(HttpStatus.BAD_REQUEST, "본인 정보만 수정할 수 있습니다."),
    EXIST_TRUE_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    MISS_MATCH_PROVIDER(HttpStatus.BAD_REQUEST, "다른 SNS 계정으로 회원가입이 돼있습니다."),
    NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 찾을 수 없습니다."),
    NOT_FOUND_PROJECT(HttpStatus.BAD_REQUEST, "프로젝트를 찾을 수 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 인증 토큰 입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN,"요청에 대한 권한이 없습니다."),
    NOT_PROJECT_MEMBER(HttpStatus.BAD_REQUEST,"프로젝트의 팀원이 아닙니다."),
    MAXIMUM_CHAT_ROOM(HttpStatus.BAD_REQUEST,"더이상 채팅방을 열 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
    NOT_FOUND_CHATROOM(HttpStatus.BAD_REQUEST,"찾을 수 없는 채팅방입니다."),
    CHATROOM_NOT_MATCHED_PROJECT(HttpStatus.BAD_REQUEST,"프로젝트에 없는 채팅방입니다."),
    ALREADY_APPLY_PROJECT(HttpStatus.BAD_REQUEST,"이미 지원한 프로젝트입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}