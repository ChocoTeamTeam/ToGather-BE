package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.SignUpControllerDto.Request;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
import chocoteamteam.togather.repository.RefreshTokenRepository;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import chocoteamteam.togather.type.TechCategory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberTechStackRepository memberTechStackRepository;
    @Mock
    TechStackRepository techStackRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    MemberService memberService;

    Member member;
    MemberTechStack memberTechStack;
    TechStack techStack;
    TechStack secondTechStack;
    MemberTechStackInfoDto memberTechStackInfoDto;
    Request modifyRequest;

    @BeforeEach
    void init() {
        techStack = TechStack.builder()
            .id(1L)
            .name("java")
            .category(TechCategory.BACKEND)
            .image("java.png")
            .build();
        secondTechStack = TechStack.builder()
            .id(2L)
            .name("react")
            .category(TechCategory.FRONTEND)
            .image("react.png")
            .build();
        member = Member.builder()
            .id(1L)
            .email("test@togather.com")
            .nickname("test")
            .profileImage("test.png")
            .status(MemberStatus.PERMITTED)
            .role(Role.ROLE_USER)
            .providerType(ProviderType.GOOGLE)
            .build();
        member.getMemberTechStacks()
            .add(MemberTechStack.builder().id(1L).techStack(techStack).build());
        memberTechStack = new MemberTechStack(1L, member, techStack);
        memberTechStackInfoDto = MemberTechStackInfoDto.builder()
            .id(1L)
            .nickname("test")
            .profileImage("test.png")
            .techId(1L)
            .techName("java")
            .techImage("java.png")
            .build();
        modifyRequest = Request.builder()
            .nickname("수정")
            .profileImage("이미지수정")
            .techStackDtos(List.of(TechStackDto.builder()
                .id(2L)
                .build()))
            .build();
    }

    @DisplayName("회원 정보 조회 성공 - 멤버 기술 스택 있음")
    @Test
    void getDetail_success_memberTechStack_exist_true() {
        // given
        given(memberTechStackRepository.findMemberWithTechStackDetailByMemberId(any()))
            .willReturn(MemberDetailResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .techStackDtos(List.of(TechStackDto.builder()
                        .id(techStack.getId())
                        .name(techStack.getName())
                        .category(techStack.getCategory())
                        .image(techStack.getImage())
                    .build()))
                .build()
            );

        // when
        MemberDetailResponse response = memberService.getDetail(1L);
        TechStackDto responseTechStackDto = response.getTechStackDtos().get(0);

        // then
        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getProfileImage()).isEqualTo(member.getProfileImage());
        assertThat(responseTechStackDto.getId()).isEqualTo(techStack.getId());
        assertThat(responseTechStackDto.getName()).isEqualTo(techStack.getName());
        assertThat(responseTechStackDto.getImage()).isEqualTo(techStack.getImage());
    }

    @DisplayName("회원 정보 조회 성공 - 멤버 기술 스택 없음")
    @Test
    void getDetail_success_memberTechStack_exist_false() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));

        // when
        MemberDetailResponse response = memberService.getDetail(1L);

        // then
        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getProfileImage()).isEqualTo(member.getProfileImage());
    }


    @DisplayName("회원 정보 조회 실패 - 존재하지 않는 회원")
    @Test
    void getDetail_failed_notFoundMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> memberService.getDetail(1L))
            .isInstanceOf(MemberException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getErrorMessage());
    }

	@DisplayName("회원 상태 변경 성공")
	@Test
	void changeStatus_success() {
		//given
		given(memberRepository.findById(any()))
			.willReturn(Optional.of(member));

		doNothing().when(refreshTokenRepository).delete(anyLong());

		//when
		memberService.changeStatus(1L,MemberStatus.WITHDRAWAL);

		//then
		assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWAL);
	}

	@DisplayName("회원 상태 변경 실패 - 파라미터가 null 인 경우")
	@Test
	void changeStatus_fail_parameterIsNull() {
		//given
		//when
		//then
		assertThatThrownBy(() -> memberService.changeStatus(null,null))
			.isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> memberService.changeStatus(1L,null))
			.isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> memberService.changeStatus(null,MemberStatus.WITHDRAWAL))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("회원 상태 변경 실패 - 회원이 조회가 안되는 경우")
	@Test
	void changeStatus_fail_memberNotFound() {
		//given
		given(memberRepository.findById(any()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> memberService.changeStatus(1L,MemberStatus.WITHDRAWAL))
			.isInstanceOf(MemberException.class)
			.hasMessage(ErrorCode.NOT_FOUND_MEMBER.getErrorMessage());
	}

    @DisplayName("회원 정보 수정 성공")
    @Test
    void modify_success() {
        // given
        memberRepository.save(member);
        techStackRepository.save(techStack);
        techStackRepository.save(secondTechStack);
        memberTechStackRepository.save(memberTechStack);
        given(memberRepository.findByNickname(any())).willReturn(Optional.ofNullable(member));
        given(memberRepository.findWithMemberTechStackById(any()))
            .willReturn(Optional.ofNullable(member));

        // when
        memberService.modify(1L, modifyRequest, 1L);

        // then
        assertThat(member.getNickname()).isEqualTo("수정");
        assertThat(member.getProfileImage()).isEqualTo("이미지수정");
    }


    @DisplayName("회원 정보 수정 실패 - 본인만 정보를 수정할 수 있음")
    @Test
    void modify_failed_miss_match_member() {
        // given

        // when

        // then
        assertThatThrownBy(() -> memberService.modify(1L, modifyRequest, 2L))
            .isInstanceOf(MemberException.class)
            .hasMessage(ErrorCode.MISS_MATCH_MEMBER.getErrorMessage());
    }

    @DisplayName("회원 정보 수정 실패 - 이미 존재하는 닉네임")
    @Test
    void modify_failed_exist_true_nickname() {
        // given
        given(memberRepository.findByNickname(any()))
            .willReturn(Optional.ofNullable(Member.builder()
                .id(2L)
                .build()));

        // when

        // then
        assertThatThrownBy(() -> memberService.modify(1L, modifyRequest, 1L))
            .isInstanceOf(MemberException.class)
            .hasMessage(ErrorCode.EXIST_TRUE_MEMBER_NICKNAME.getErrorMessage());
    }

    @DisplayName("회원 정보 수정 실패 - 회원이 존재하지 않음")
    @Test
    void modify_failed_not_found_member() {
        // given
        given(memberRepository.findByNickname(any()))
            .willReturn(Optional.ofNullable(Member.builder()
                .id(1L)
                .build()));

        // when

        // then
        assertThatThrownBy(() -> memberService.modify(1L, modifyRequest, 1L))
            .isInstanceOf(MemberException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getErrorMessage());
    }

    @DisplayName("닉네임 중복 검사 - 성공")
    @Test
    void existNickname_success() {
        // given
        given(memberRepository.existsByNickname(any())).willReturn(true);

        // when
        boolean response = memberService.existNickname("test");

        // then
        assertThat(response).isTrue();
    }

}