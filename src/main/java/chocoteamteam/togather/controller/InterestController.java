package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Interest", description = "관심 공고 API")
@RestController
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @Operation(
        summary = "관심 공고 추가, 취소 api", description = "특정 프로젝트를 관심있는 프로젝트 등록하거나 등록돼있는 상태면 취소합니다.",
        security = {@SecurityRequirement(name = "Authorization")}, tags = {"Interest"}
    )
    @PostMapping("/projects/{projectId}/interest")
    public ResponseEntity<?> addOrRemove(@PathVariable @Positive Long projectId,
        @AuthenticationPrincipal LoginMember loginMember) {
        interestService.addOrRemove(projectId, loginMember.getId());
        return ResponseEntity.ok().body("");
    }

    @Operation(
        summary = "관심 공고 조회", description = "관심 프로젝트로 등록한 프로젝들을 조회 합니다.",
        security = {@SecurityRequirement(name = "Authorization")}, tags = {"Interest"}
    )
    @GetMapping("/members/{memberId}/interests")
    public ResponseEntity<?> getDetails(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(interestService.getDetails(loginMember.getId()));
    }

}
