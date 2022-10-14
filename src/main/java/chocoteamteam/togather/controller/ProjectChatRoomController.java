package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.ChangeChatRoomNameForm;
import chocoteamteam.togather.dto.ChatDetailDto;
import chocoteamteam.togather.dto.ChatRoomDto;
import chocoteamteam.togather.dto.ChatRoomsResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public ResponseEntity<ChatRoomDto> createProjectChat(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable long projectId, @RequestBody @Valid CreateChatRoomForm form) {

		form.setProjectId(projectId);
		form.setMemberId(member.getId());

		return ResponseEntity.ok().body(projectChatRoomService.createChatRoom(form));
	}

	@Operation(
		summary = "채팅방 리스트 조회", description = "프로젝트 멤버만 조회가능",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Chat"}
	)
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{projectId}/chats")
	public ResponseEntity<ChatRoomsResponse> getProjectChats(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable long projectId) {

        return ResponseEntity.ok().body(new ChatRoomsResponse(
                projectId,
                projectChatRoomService.getChatRooms(projectId, member.getId()))
            );
    }

	// 채팅방 상세조회
	@Operation(
		summary = "채팅방 상세 조회", description = "프로젝트 멤버만 조회가능, 메시지들도 함께 조회",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Chat"}
	)
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{projectId}/chats/{chatId}")
	public ResponseEntity<ChatDetailDto> getProjectChat(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable long projectId, @PathVariable long chatId) {

		return ResponseEntity.ok()
			.body(projectChatRoomService
				.getChatRoom(projectId, member.getId(), chatId));
	}

	@Operation(
		summary = "채팅방 이름 수정", description = "채팅방 이름 수정",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Chat"}
	)
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/{projectId}/chats/{chatId}")
	public ResponseEntity changeProjectChatName(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable long projectId, @PathVariable long chatId,
		@RequestBody ChangeChatRoomNameForm form) {

		form.setProjectId(projectId);
		form.setMemberId(member.getId());
		form.setRoomId(chatId);

		projectChatRoomService.changeChatRoomName(form);

		return ResponseEntity.ok().body("");
	}

	@Operation(
		summary = "채팅방 삭제", description = "채팅방 삭제 시, 저장된 메시지도 함께 삭제",
		security = {@SecurityRequirement(name = "Authorization")},
		tags = {"Chat"}
	)
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{projectId}/chats/{chatId}")
	public ResponseEntity deleteProjectChat(
		@ApiIgnore @AuthenticationPrincipal LoginMember member,
		@PathVariable long projectId, @PathVariable long chatId) {

		projectChatRoomService.deleteChatRoom(projectId, member.getId(), chatId);

		return ResponseEntity.ok().body("");
	}


}
