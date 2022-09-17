package chocoteamteam.togather.dto;


import chocoteamteam.togather.type.ProjectStatus;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCondition {

    /*  검색 키워드 - option
     * */
    private String title;
    private String content;
    private String author;

    /*  필터링 키워드 - option
     * */
    private ProjectStatus projectStatus;
    private List<Long> skillsId;

    /*  반환 size - 필수
     * */
    @NotNull
    @Min(1)
    @Max(100)
    private Long limit;

    /*  페이지 번호 - option - default 0
     * */
    private long pageNumber;
}