package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.MemberTechStack;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.repository.MemberRepository;
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

    @InjectMocks
    MemberService memberService;

    Member member;
    MemberTechStack memberTechStack;
    TechStack techStack;

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
    }

    @DisplayName("내 정보 조회 성공")
    @Test
    void getDetail_success() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
        given(techStackRepository.findAllById(any())).willReturn(List.of(techStack));

        // when
        MemberDetailResponse response = memberService.getDetail(1L);
        TechStackDto responseTechStackDto = response.getTechStackDtos().get(0);

        // then
        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getProfileImage()).isEqualTo(member.getProfileImage());
        assertThat(responseTechStackDto.getId()).isEqualTo(techStack.getId());
        assertThat(responseTechStackDto.getName()).isEqualTo(techStack.getName());
        assertThat(responseTechStackDto.getCategory()).isEqualTo(techStack.getCategory());
        assertThat(responseTechStackDto.getImage()).isEqualTo(techStack.getImage());
    }


    @DisplayName("내 정보 조회 실패 - 존재하지 않는 회원")
    @Test
    void getDetail_failed_notFoundMember() {
        // given

        // when

        // then
        assertThatThrownBy(() -> memberService.getDetail(1L))
            .isInstanceOf(MemberException.class)
            .hasMessage("존재하지 않는 회원입니다.");
    }


}