package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.MemberTechStack;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberTechStackRepository extends JpaRepository<MemberTechStack, Long>, MemberTechStackCustomRepository {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from MemberTechStack mts where mts.id in :ids")
    void deleteAllByIdInQuery(@Param("ids")  Iterable<Long> ids);

}
