package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.ChangeChatRoomNameForm;
import chocoteamteam.togather.dto.ChatDetailDto;
import chocoteamteam.togather.dto.ChatRoomDto;
import chocoteamteam.togather.dto.CreateChatRoomForm;
import chocoteamteam.togather.entity.ChatRoom;
import chocoteamteam.togather.exception.ChatRoomException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectMemberException;
import chocoteamteam.togather.repository.ChatRoomRepository;
import chocoteamteam.togather.repository.ProjectMemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.impl.QuerydslChatRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectChatRoomService {

	private static final int TEAM_CHAT_MAXIMUM = 5;

	private final ChatRoomRepository chatRoomRepository;
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final QuerydslChatRepository querydslChatRepository;


	@Transactional
	public ChatRoomDto createChatRoom(@NonNull CreateChatRoomForm form) {
		authenticateProjectMember(form.getProjectId(), form.getMemberId());

		checkChatRoomMaximum(form.getProjectId());

		return ChatRoomDto.from(chatRoomRepository.save(ChatRoom.builder()
			.project(projectRepository.getReferenceById(form.getProjectId()))
			.name(form.getRoomName())
			.build()));
	}
	private void authenticateProjectMember(long projectId, long memberId) {
		if (!projectMemberRepository.existsByProject_IdAndMember_Id(projectId, memberId)) {
			throw new ProjectMemberException(ErrorCode.NO_PERMISSION);
		}
	}
	private void checkChatRoomMaximum(long projectId) {
		if (chatRoomRepository.countByProject_Id(projectId) >= TEAM_CHAT_MAXIMUM) {
			throw new ChatRoomException(ErrorCode.MAXIMUM_CHAT_ROOM);
		}
	}

	@Transactional(readOnly = true)
	public List<ChatRoomDto> getChatRooms(long projectId, long memberId) {
		authenticateProjectMember(projectId,memberId);

		return ChatRoomDto.of(chatRoomRepository.findAllByProject_Id(projectId));
	}

	@Transactional(readOnly = true)
	public ChatDetailDto getChatRoom(long projectId, long memberId, long chatRoomId) {
		authenticateProjectMember(projectId, memberId);

		ChatRoom chatRoom = getProjectChat(projectId, chatRoomId);

		return ChatDetailDto.builder()
			.roomId(chatRoomId)
			.roomName(chatRoom.getName())
			.messages(querydslChatRepository.findAllByChatRoomId(chatRoomId))
			.build();

	}

	@Transactional
	public void changeChatRoomName(ChangeChatRoomNameForm form) {
		authenticateProjectMember(form.getProjectId(), form.getMemberId());

		ChatRoom chatRoom = getProjectChat(form.getProjectId(), form.getRoomId());

		chatRoom.changeName(form.getRoomName());
	}

	private ChatRoom getProjectChat(long projectId, long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new ChatRoomException(ErrorCode.NOT_FOUND_CHATROOM));

		if (projectId != chatRoom.getProject().getId()) {
			throw new ChatRoomException(ErrorCode.CHATROOM_NOT_MATCHED_PROJECT);
		}
		return chatRoom;
	}

}
