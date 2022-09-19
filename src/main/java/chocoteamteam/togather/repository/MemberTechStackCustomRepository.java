package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.queryDslSimpleDto.MemberTechStackInfoDto;
import java.util.List;

public interface MemberTechStackCustomRepository {

    List<MemberTechStackInfoDto> findAllByMemberId(Long id);

}
