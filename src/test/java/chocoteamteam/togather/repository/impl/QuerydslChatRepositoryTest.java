package chocoteamteam.togather.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import chocoteamteam.togather.dto.ChatMessageDto;
import chocoteamteam.togather.entity.ChatMessage;
import chocoteamteam.togather.entity.ChatRoom;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.ChatMessageRepository;
import chocoteamteam.togather.repository.ChatRoomRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({QueryDslTestConfig.class,QuerydslChatRepository.class})
@DataJpaTest
class QuerydslChatRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;
	@Autowired
	ChatMessageRepository chatMessageRepository;
	@Autowired
	QuerydslChatRepository querydslChatRepository;

	Member member;

	@BeforeAll
	@Transactional
	public void beforeAll() {
		member = Member.builder()
			.status(MemberStatus.PERMITTED)
			.role(Role.ROLE_USER)
			.email("test@test.com")
			.profileImage("noImage")
			.providerType(ProviderType.KAKAO)
			.nickname("tester")
			.build();

		memberRepository.save(member);

		Project project = Project.builder()
			.member(member)
			.build();

		projectRepository.save(project);

		ChatRoom chatRoom = ChatRoom.builder()
			.project(project)
			.name("testChatRoom")
			.build();

		chatRoomRepository.save(chatRoom);

		for (int i = 0; i < 10000; i++) {
			ChatMessage chatMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.sender(member)
				.message("test")
				.build();

			chatMessageRepository.save(chatMessage);
		}
	}
	@DisplayName("채팅방 채팅메시지 조회 성공 - 최신순으로, 최대 1000건")
	@Test
	@Order(1)
	@Transactional
	void findAllByTeamId_success() {

		List<ChatMessageDto> result = querydslChatRepository.findAllByChatRoomId(1L);
		ChatMessageDto chatMessageDto = result.get(0);

		assertThat(result.size()).isEqualTo(1000);
		assertThat(chatMessageDto.getNickname()).isEqualTo(member.getNickname());
		assertThat(chatMessageDto.getProfileImage()).isEqualTo(member.getProfileImage());
		assertThat(chatMessageDto.getMessage()).isEqualTo("test");
	}

	@DisplayName("채팅방 채팅메시지 삭제 성공")
	@Test
	@Order(2)
	@Transactional
	void deleteAllByChatRoomId_success() {

		long delCnt = querydslChatRepository.deleteAllByChatRoomId(1L);

		assertThat(delCnt).isEqualTo(10000);
	}

}