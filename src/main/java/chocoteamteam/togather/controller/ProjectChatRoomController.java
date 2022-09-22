package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.ChatRoomDto;
import chocoteamteam.togather.dto.CreateChatRoomForm;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.ProjectChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "Chat", description = "채팅 관련 API")
@RequiredArgsConstructor
@RequestMapping("/projects")
@RestController
public class ProjectChatRoomController {

	private final ProjectChatRoomService projectChatRoomService;

	@Operation(
		summary = "채팅방 생성", description = "프로젝트 멤버만 생성 가능, 최대 5개까지 생성 가능",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Chat"}
	)
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/{projectId}/chats")
	public ResponseEntity<ChatRoomDto> createProjectChat(@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable Long projectId,@RequestBody @Valid CreateChatRoomForm form) {

		form.setProjectId(projectId);
		form.setMemberId(member.getId());

		return ResponseEntity.ok().body(projectChatRoomService.createChatRoom(form));
	}


}