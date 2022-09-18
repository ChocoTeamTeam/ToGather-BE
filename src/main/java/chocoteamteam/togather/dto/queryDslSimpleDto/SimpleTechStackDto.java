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
public class SimpleTechStackDto {
    private Long id;
    private String name;
    private String image;

    @QueryProjection
    public SimpleTechStackDto(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}