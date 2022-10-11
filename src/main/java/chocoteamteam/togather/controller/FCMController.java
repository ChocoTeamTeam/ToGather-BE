package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.FCMRegisterTokenForm;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FCM_Notification", description = "FCM 알림 관련 API")
@RequiredArgsConstructor
@RestController
public class FCMController {

	private final FCMService fcmService;

	@Operation(
		summary = "Registration Token 저장",
		description = "사용자 ID를 기반으로 Token을 저장합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"FCM_Notification"}
	)
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/fcm/token")
	public ResponseEntity registerFCMToken(@AuthenticationPrincipal LoginMember member,
		@Valid @RequestBody FCMRegisterTokenForm form) {

		fcmService.saveRegistrationToken(member.getId(), form.getRegistrationToken());

		return ResponseEntity.ok().body("success");
	}

	@Operation(
		summary = "Registration Token 삭제",
		description = "사용자 ID를 기반으로 Token을 삭제합니다.",
		security = {@SecurityRequirement(name = "Authorization")}, tags = {"FCM_Notification"}
	)
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/fcm/token")
	public ResponseEntity deleteFCMToken(@AuthenticationPrincipal LoginMember member) {

		fcmService.deleteRegistrationToken(member.getId());

		return ResponseEntity.ok().body("success");
	}

}
