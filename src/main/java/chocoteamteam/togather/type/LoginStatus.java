package chocoteamteam.togather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginStatus {

    EXIST("기존 회원", true),
    NEW("신규 회원", false);

    private String message;
    private boolean foundMember;
}
