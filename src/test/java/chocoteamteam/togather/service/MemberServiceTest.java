package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.MemberTechStackCustomRepository;
import chocoteamteam.togather.repository.MemberTechStackRepository;
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
    MemberTechStackCustomRepository memberTechStackCustomRepository;

    @InjectMocks
    MemberService memberService;

    Member member;
    MemberTechStack memberTechStack;
    TechStack techStack;
    MemberTechStackInfoDto memberTechStackInfoDto;

    @BeforeEach
    void init() {
        techStack = TechStack.builder()
            .id(1L)
            .name("java")
            .category(TechCategory.BACKEND)
            .image("java.png")
            .build();
        member = Member.builder()
            .id(1L)
            .email("test@togather.com")
            .nickname("test")
            .profileImage("test.png")
            .status(MemberStatus.PERMITTED)
            .role(Role.ROLE_USER)
            .providerType(ProviderType.GOOGLE)
            .memberTechStacks(
                List.of(MemberTechStack.builder().id(1L).techStack(techStack).build()))
            .build();
        memberTechStack = new MemberTechStack(1L, member, techStack);
        memberTechStackInfoDto = MemberTechStackInfoDto.builder()
            .id(1L)
            .nickname("test")
            .profileImage("test.png")
            .techName("java")
            .techImage("java.png")
            .build();
    }

    @DisplayName("회원 정보 조회 성공 - 멤버 기술 스택 있음")
    @Test
    void getDetail_success_memberTechStack_exist_true() {
        // given
        given(memberTechStackCustomRepository.findAllByMemberId(any()))
            .willReturn(List.of(memberTechStackInfoDto));

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


}