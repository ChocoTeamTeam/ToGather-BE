package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.LoginResponse;
import chocoteamteam.togather.dto.SignUpControllerDto;
import chocoteamteam.togather.dto.SignUpServiceDto;
import chocoteamteam.togather.service.OAuthService;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth")
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login/{provider}")
    public LoginResponse login(@RequestBody String code, @PathVariable String provider) {
        return oAuthService.login(code, provider);
    }

    @PostMapping("/signup")
    public SignUpControllerDto.Response signup(
        @RequestBody @Valid SignUpControllerDto.Request request,
        @NotEmpty @RequestHeader(value = "signUpToken") String signUpToken) {
        return oAuthService.signUp(SignUpServiceDto.builder()
            .signUpToken(signUpToken)
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .techStackDtoList(request.getTechStackDtos())
            .build());
    }

}
