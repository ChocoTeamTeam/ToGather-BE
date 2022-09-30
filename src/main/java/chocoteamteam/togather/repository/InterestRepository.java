package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Interest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByMemberIdAndProjectId(Long memberId, Long projectId);

    long countByMemberId(Long memberId);

    List<Interest> findAllByMemberId(Long memberId);

}
