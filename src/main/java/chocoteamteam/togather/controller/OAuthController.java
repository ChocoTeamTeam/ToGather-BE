package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.LoginResponse;
import chocoteamteam.togather.dto.SignUpControllerDto;
import chocoteamteam.togather.dto.SignUpServiceDto;
import chocoteamteam.togather.service.OAuthService;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/oauth/{provider}")
    public LoginResponse login(@RequestParam String code, @PathVariable String provider) {
        return oAuthService.login(code, provider);
    }

    @Valid
    @PostMapping("/oauth/signup")
    public SignUpControllerDto.Response signup(@RequestBody SignUpControllerDto.Request request,
        @NotEmpty @RequestHeader(value = "signUpToken") String signUpToken) {
        return oAuthService.signUp(SignUpServiceDto.builder()
            .signUpToken(signUpToken)
            .nickname(request.getNickname())
            .profileImage(request.getProfileImage())
            .techStackDtoList(request.getTechStackDtos())
            .build());
    }

}
