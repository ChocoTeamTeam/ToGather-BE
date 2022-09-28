package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.CreateInterestForm;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    private final InterestService interestService;

    @Operation(
        summary = "관심 공고 추가 api", description = "특정 프로젝트를 관심있는 프로젝트로 등록합니다.",
        security = {@SecurityRequirement(name = "Authorization")}, tags = {"Interest"}
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid CreateInterestForm form, @AuthenticationPrincipal LoginMember loginMember) {
        interestService.add(form.getMemberId(), form.getProjectId(), loginMember.getId());
        return ResponseEntity.ok().body("");
    }

}
