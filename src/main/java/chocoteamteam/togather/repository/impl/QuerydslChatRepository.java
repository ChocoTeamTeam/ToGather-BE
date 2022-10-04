package chocoteamteam.togather.repository.impl;

import static chocoteamteam.togather.entity.QChatMessage.chatMessage;
import static chocoteamteam.togather.entity.QMember.member;

import chocoteamteam.togather.dto.ChatMessageDto;
import chocoteamteam.togather.entity.QChatMessage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Repository
@Transactional
public class QuerydslChatRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;

	public List<ChatMessageDto> findAllByChatRoomId(long chatRoomId) {
		List<ChatMessageDto> result = jpaQueryFactory.select(
				Projections.fields(ChatMessageDto.class,
					member.nickname.as("nickname"),
					member.profileImage.as("profileImage"),
					chatMessage.message.as("message"),
					chatMessage.createdAt.as("sendTime")
				)).from(chatMessage)
			.innerJoin(chatMessage.sender, member)
			.where(chatMessage.chatRoom.id.eq(chatRoomId))
			.orderBy(chatMessage.createdAt.desc())
			.limit(1000)
			.fetch();

		return result;
	}

	public long deleteAllByChatRoomId(long chatRoomId) {
		QChatMessage subChatMessage = new QChatMessage("subChatMessage");

		long deleteCnt = jpaQueryFactory.delete(chatMessage)
			.where(chatMessage.id.in(JPAExpressions.select(subChatMessage.id)
				.from(subChatMessage)
				.where(subChatMessage.chatRoom.id.eq(chatRoomId))))
			.execute();

		entityManager.flush();
		entityManager.clear();

		return deleteCnt;
	}



}
