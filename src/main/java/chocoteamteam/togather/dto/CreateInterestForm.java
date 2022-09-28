package chocoteamteam.togather.dto;

import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateInterestForm {

    @Positive
    private Long memberId;
    @Positive
    private Long projectId;


}
