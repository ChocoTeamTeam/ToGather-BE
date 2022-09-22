package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.dto.ChatRoomDto;
import chocoteamteam.togather.dto.CreateChatRoomForm;
import chocoteamteam.togather.entity.ChatRoom;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ChatRoomException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectMemberException;
import chocoteamteam.togather.repository.ChatRoomRepository;
import chocoteamteam.togather.repository.ProjectMemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectChatRoomServiceTest {

	@Mock
	ChatRoomRepository chatRoomRepository;
	@Mock
	ProjectRepository projectRepository;
	@Mock
	ProjectMemberRepository projectMemberRepository;

	@InjectMocks
	ProjectChatRoomService projectChatRoomService;

	ChatRoom chatRoom;
	CreateChatRoomForm form;

	@BeforeEach
	public void init() {
		chatRoom = ChatRoom.builder()
			.id(1L)
			.project(Project.builder().id(1L).build())
			.name("ChatName")
			.build();

		form = CreateChatRoomForm.builder()
			.projectId(1L)
			.memberId(1L)
			.roomName("ChatName")
			.build();
	}


	@DisplayName("프로젝트 채팅방 생성 성공")
	@Test
	void createChatRoom_success() {
		//given
		given(projectMemberRepository.existsByProject_IdAndMember_Id(anyLong(), anyLong()))
			.willReturn(true);
		given(chatRoomRepository.countByProject_Id(anyLong()))
			.willReturn(4L);
		given(chatRoomRepository.save(any()))
			.willReturn(chatRoom);

		//when
		ChatRoomDto dto = projectChatRoomService.createChatRoom(form);

		//then
		assertThat(dto.getRoomId()).isEqualTo(chatRoom.getId());
		assertThat(dto.getRoomName()).isEqualTo(chatRoom.getName());
	}

	@DisplayName("프로젝트 채팅방 생성 실패 - 프로젝트 멤버가 아닌 경우")
	@Test
	void createChatRoom_fail_notProjectMember() {
		//given
		given(projectMemberRepository.existsByProject_IdAndMember_Id(anyLong(), anyLong()))
			.willReturn(false);

		//when
		//then
		assertThatThrownBy(() -> projectChatRoomService.createChatRoom(form))
			.isInstanceOf(ProjectMemberException.class)
			.hasMessage(ErrorCode.NOT_PROJECT_MEMBER.getErrorMessage());
	}

	@DisplayName("프로젝트 채팅방 생성 실패 - 이미 채팅방을 최대치만큼 생성한 경우")
	@Test
	void createChatRoom_fail_maximumChatRoom() {
		//given
		given(projectMemberRepository.existsByProject_IdAndMember_Id(anyLong(), anyLong()))
			.willReturn(true);
		given(chatRoomRepository.countByProject_Id(anyLong()))
			.willReturn(5L);

		//when
		//then
		assertThatThrownBy(() -> projectChatRoomService.createChatRoom(form))
			.isInstanceOf(ChatRoomException.class)
			.hasMessage(ErrorCode.MAXIMUM_CHAT_ROOM.getErrorMessage());
	}

	@DisplayName("프로젝트 채팅방 리스트 조회 성공")
	@Test
	void getChatRooms_success(){
	    //given
		List<ChatRoom> list = Arrays.asList(chatRoom);

		given(projectMemberRepository.existsByProject_IdAndMember_Id(anyLong(), anyLong()))
			.willReturn(true);
		given(chatRoomRepository.findByProject_Id(anyLong()))
			.willReturn(list);

	    //when
		List<ChatRoomDto> chatRooms = projectChatRoomService.getChatRooms(1L, 1L);

		//then
		assertThat(chatRooms.size()).isEqualTo(list.size());
		assertThat(chatRooms.get(0).getRoomId()).isEqualTo(chatRoom.getId());
		assertThat(chatRooms.get(0).getRoomName()).isEqualTo(chatRoom.getName());
	}

	@DisplayName("프로젝트 채팅방 리스트 조회 실패 - 프로젝트 멤버가 아닌 경우")
	@Test
	void getChatRooms_fail_notProjectMember(){
	    //given
		given(projectMemberRepository.existsByProject_IdAndMember_Id(anyLong(), anyLong()))
			.willReturn(false);

	    //when
		//then
		assertThatThrownBy(() -> projectChatRoomService.getChatRooms(1L, 1L))
			.isInstanceOf(ProjectMemberException.class)
			.hasMessage(ErrorCode.NOT_PROJECT_MEMBER.getErrorMessage());
	}


}