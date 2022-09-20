package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.SignUpControllerDto;
import chocoteamteam.togather.service.MemberService;
import chocoteamteam.togather.type.MemberStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 관련 API")
@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "회원 정보 조회 api", description = "회원 아이디 값으로 회원을 조회하고 회원이 존재 할 경우 해당 회원의 정보를 응답합니다.",
        security = {@SecurityRequirement(name = "Authorization")}, tags = {"Member"}
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> getDetail(@PathVariable @Positive Long memberId) {
        return ResponseEntity.ok(memberService.getDetail(memberId));
    }

	@Operation(
		summary = "회원 탈퇴", description = "유저 본인 혹은 어드민이 회원탈퇴를 진행할 수 있습니다.",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Member"}
	)
	@PreAuthorize("hasRole('USER') and principal.id == #memberId or hasRole('ADMIN')")
	@PostMapping("/{memberId}/withdrawal")
	public ResponseEntity withdrawal(@PathVariable("memberId") Long memberId) {

		memberService.changeStatus(memberId, MemberStatus.WITHDRAWAL);

		return ResponseEntity.ok().body("");
	}

	@Operation(
		summary = "회원 정보 수정 Api", description = "로그인한 회원과 요청한 회원을 비교하고 중복되는 닉네임이 없다면 해당 회원의 정보를 수정합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"Member"}
	)
	@PutMapping("/{memberId}")
	public ResponseEntity<?> modify(@PathVariable @Positive Long memberId, @RequestBody @Valid
	SignUpControllerDto.Request request, @AuthenticationPrincipal LoginMember loginMember) {
		memberService.modify(memberId, request, loginMember.getId());
		return ResponseEntity.ok().body("");
	}

}

