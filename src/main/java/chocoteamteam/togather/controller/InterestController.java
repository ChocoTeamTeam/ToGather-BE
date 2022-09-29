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
        summary = "관심 공고 추가, 취소 api", description = "특정 프로젝트를 관심있는 프로젝트 등록하거나 등록돼있는 상태면 취소합니다.",
        security = {@SecurityRequirement(name = "Authorization")}, tags = {"Interest"}
    )
    @PostMapping
    public ResponseEntity<?> addOrRemove(@RequestBody @Valid CreateInterestForm form, @AuthenticationPrincipal LoginMember loginMember) {
        interestService.addOrRemove(form.getMemberId(), form.getProjectId(),
            loginMember.getId());
        return ResponseEntity.ok().body("");
    }

}
