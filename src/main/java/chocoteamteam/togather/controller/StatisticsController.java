package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.WeeklyTechStackStatisticsResponse;
import chocoteamteam.togather.service.TechStackStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TechStack Statistics", description = "주단위 기술스택 통계 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/techStackStatistics")
public class StatisticsController {

    private final TechStackStatisticsService techStackStatisticsService;

    @Operation(summary = "주간 기술스택 통계 조회 api",
            description = "통계자료를 반환합니다.",
            tags = {"TechStack Statistics"})
    @GetMapping
    public ResponseEntity<WeeklyTechStackStatisticsResponse> getWeeklyTechStackStatistics() {
        return ResponseEntity.ok(techStackStatisticsService.getWeeklyStatistics());
    }

    @Operation(summary = "특정 주차 기술스택 통계 조회 api",
            description = "특정 주차의 통계자료를 반환합니다.",
            tags = {"TechStack Statistics by weeks"})
    @GetMapping("/{weeks}")
    public ResponseEntity<WeeklyTechStackStatisticsResponse> getTechStackStatisticsByWeeks(@PathVariable Integer weeks) {
        return ResponseEntity.ok(techStackStatisticsService.getWeeklyStatistics(weeks));
    }

}
