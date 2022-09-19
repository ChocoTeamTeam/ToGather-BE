package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.dto.LoginResponse;
import chocoteamteam.togather.dto.SignUpControllerDto;
import chocoteamteam.togather.dto.SignUpServiceDto;
import chocoteamteam.togather.service.JwtService;
import chocoteamteam.togather.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "인증 관련 API")
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


    @Operation(
        summary = "로그아웃", description = "로그인 되어있는 유저나 어드민은 로그아웃 할 수 있습니다.",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"OAuth"}
    )
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/logout")
    public ResponseEntity logout(@AuthenticationPrincipal LoginMember loginMember) {
        oAuthService.logout(loginMember.getId());
        return ResponseEntity.ok().body("");
    }
}
