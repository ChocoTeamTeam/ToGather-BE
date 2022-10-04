package chocoteamteam.togather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ParticipatingProjectDto {
    private Long projectId;
    private String title;
}
