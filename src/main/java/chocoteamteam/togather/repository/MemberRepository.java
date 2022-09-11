package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
