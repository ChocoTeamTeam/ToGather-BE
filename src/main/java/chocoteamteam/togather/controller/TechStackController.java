package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.service.TechStackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "TechStack", description = "기술스택 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/techStacks")
public class TechStackController {

    private final TechStackService techStackService;

    @Operation(
            summary = "기술스택 추가",
            description = "기술스택을 추가합니다. ADMIN만 추가할 수 있습니다",
            security = {@SecurityRequirement(name = "Authorization")}, tags = {"TechStack"}
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TechStackDto> createTechStack(
            @Valid @RequestBody CreateTechStackForm form
    ) {
        return ResponseEntity.ok(techStackService.createTechStack(form));
    }

    @Operation(
            summary = "기술스택 목록 조회",
            description = "기술스택 목록을 조회합니다",
            tags = {"TechStack"}
    )
    @GetMapping
    public ResponseEntity<List<TechStackDto>> getTechStacks() {
        return ResponseEntity.ok(techStackService.getTechStacks());
    }
}
