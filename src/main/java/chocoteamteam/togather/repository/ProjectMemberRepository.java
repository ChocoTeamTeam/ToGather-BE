package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {

	boolean existsByProject_IdAndMember_Id(Long projectId, Long memberId);
}
