package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.type.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, QueryDslProjectRepository {

    Page<Project> findAllByStatusAndDeadlineBefore(ProjectStatus status, LocalDate deadline, Pageable pageable);

    @Query(value = "select * from project as p " +
            "where round(degrees(acos(sin(radians(:latitude)) * sin(radians(p.latitude)) + (cos(radians( :latitude )) * cos(radians(p.latitude)) * cos(radians( :longitude - p.longitude)))))" +
            " * 60 * 1.1515 * 1.609344, 4) < :distance" ,nativeQuery = true)
    List<Project> findAllByDistance(Integer distance, Double latitude, Double longitude);
}