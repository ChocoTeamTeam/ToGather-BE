package chocoteamteam.togather.dto.queryDslSimpleDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SimpleMemberDto {
    private Long id;
    private String nickname;
    private String profileImage;

    @QueryProjection
    public SimpleMemberDto(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}