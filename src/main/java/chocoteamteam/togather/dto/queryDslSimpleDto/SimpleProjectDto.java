package chocoteamteam.togather.dto.queryDslSimpleDto;


import chocoteamteam.togather.type.ProjectStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleProjectDto {
    private Long id;
    private SimpleMemberDto member;
    private String title;
    private Integer personnel;
    private ProjectStatus status;
    private LocalDate deadline;

    private List<SimpleTechStackDto> techStacks;

    /*  queryDsl 전용
     * */
    @QueryProjection
    public SimpleProjectDto(Long id, SimpleMemberDto member, String title, Integer personnel, ProjectStatus status, LocalDate deadline,
                            List<SimpleTechStackDto> techStacks) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.personnel = personnel;
        this.status = status;
        this.deadline = deadline;
        this.techStacks = techStacks;
    }
}