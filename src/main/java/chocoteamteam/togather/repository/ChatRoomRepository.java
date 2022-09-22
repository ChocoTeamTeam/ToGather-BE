package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	long countByProject_Id(long projectId);
}
