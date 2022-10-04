package chocoteamteam.togather.batch.application.model;

import chocoteamteam.togather.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMemberTechStackInfoDto {
    private String email;
    private List<Long> techStackIds;

    public SimpleMemberTechStackInfoDto(Member member) {
        this.email = member.getEmail();
        this.techStackIds = member.getMemberTechStacks().stream()
                .map(x -> x.getTechStack().getId())
                .collect(Collectors.toList());
    }
}
