package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.MemberDetailResponse;
import chocoteamteam.togather.service.MemberService;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> getDetail(@PathVariable @Positive Long memberId) {
        return ResponseEntity.ok(memberService.getDetail(memberId));
    }

}