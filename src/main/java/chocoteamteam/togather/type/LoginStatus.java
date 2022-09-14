package chocoteamteam.togather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginStatus {

    EXISTING_MEMBER("기존 회원", true),
    NEW_MEMBER("신규 회원", false);

    private String message;
    private boolean foundMember;
}
