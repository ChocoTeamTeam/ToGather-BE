package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"memberTechStacks"}, type = EntityGraphType.LOAD)
    Optional<Member> findWithMemberTechStackById(Long id);

    boolean existsByNickname(String nickname);

}
