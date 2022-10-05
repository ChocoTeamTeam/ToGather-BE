package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Location;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.repository.impl.QueryDslTestConfig;
import chocoteamteam.togather.type.ProjectStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@ExtendWith(SpringExtension.class)
class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("프로젝트 지도 거리 정보로 검색")
    void native_query_test() {
        //given
        Member member = memberRepository.save( Member.builder()
                .email("togather@to.com")
                .nickname("두개더")
                .profileImage("img_url")
                .build());

        projectRepository.save(Project.builder()
                .member(member)
                .title("제목999")
                .personnel(10)
                .status(ProjectStatus.RECRUITING)
                .offline(true)
                .location(Location.builder()
                        .address("서울특별시 강남구 센터 테헤란로 231 필드 웨스트 6층 7층")
                        .latitude(37.503050)
                        .longitude(127.041583)
                        .build())
                .build());

        projectRepository.save(Project.builder()
                .member(member)
                .title("제목2")
                .personnel(5)
                .status(ProjectStatus.RECRUITING)
                .offline(true)
                .location(Location.builder()
                        .address("강남역 12번출구")
                        .latitude(37.498426)
                        .longitude(127.028638)
                        .build())
                .build());

        //when
        int distance = 1;
        double latitude = 37.497009;
        double longitude = 127.024174;

        List<Project> projects = projectRepository.findAllByDistance(distance, latitude, longitude);
        //then

        assertEquals(1, projects.size());
        assertEquals("강남역 12번출구", projects.get(0).getLocation().getAddress());
    }

}