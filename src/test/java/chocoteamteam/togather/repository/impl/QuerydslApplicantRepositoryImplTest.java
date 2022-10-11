package chocoteamteam.togather.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chocoteamteam.togather.DataCleanUp;
import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.Applicant;
import chocoteamteam.togather.entity.ChatMessage;
import chocoteamteam.togather.entity.ChatRoom;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.ApplicantRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import chocoteamteam.togather.type.TechCategory;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({QueryDslTestConfig.class,DataCleanUp.class})
@DataJpaTest
class QuerydslApplicantRepositoryImplTest {


	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TechStackRepository techStackRepository;

	@Autowired
	MemberTechStackRepository memberTechStackRepository;

	@Autowired
	ApplicantRepository applicantRepository;

	@Autowired
	QuerydslApplicantRepositoryImpl querydslApplicantRepository;

	Member member;
	Project project;

	TechStack techStack1;
	TechStack techStack2;

	Applicant applicant;

	@Autowired
	private DataCleanUp dataCleanUp;

	@BeforeAll
	@Transactional
	public void beforeAll() {
		dataCleanUp.execute();

		member = memberRepository.save(
			Member.builder()
				.nickname("닉네임")
				.email("이메일")
				.profileImage("이미지")
				.build());

		project = projectRepository.save(
			Project.builder()
				.member(member)
				.build());

		techStack1 = techStackRepository.save(
			TechStack.builder()
				.name("자바")
				.category(TechCategory.BACKEND)
				.image("자바 이미지")
				.build()
		);
		techStack2 = techStackRepository.save(
			TechStack.builder()
				.name("리액트")
				.category(TechCategory.FRONTEND)
				.image("리액트 이미지")
				.build()
		);

		for (int i = 0; i < 10; i++) {
			// 회원 저장
			Member tempMember = memberRepository.save(
				Member.builder()
					.nickname("test" + i)
					.email("email"+i)
					.profileImage("image")
					.build());

			// 회원 테크 스택 저장
			memberTechStackRepository.save(
				MemberTechStack.builder()
					.techStack(techStack1)
					.member(tempMember)
					.build()
			);

			memberTechStackRepository.save(
				MemberTechStack.builder()
					.techStack(techStack2)
					.member(tempMember)
					.build()
			);

			applicant = applicantRepository.save(
				Applicant.builder()
					.project(project)
					.member(tempMember)
					.status(ApplicantStatus.WAIT)
					.build()
			);
		}
	}

	@DisplayName("ApplicantDto , 10건 조회 ")
	@Test
	void findAllByProjectId() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<ApplicantDto> result = querydslApplicantRepository.findAllByProjectId(1L,
			ApplicantStatus.WAIT);

		stopWatch.stop();
		System.out.println("시간 = "+ stopWatch.getTotalTimeMillis());

		assertThat(result.size()).isEqualTo(10);
	}
}