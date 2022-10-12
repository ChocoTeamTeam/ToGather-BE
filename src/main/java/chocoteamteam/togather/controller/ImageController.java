package chocoteamteam.togather.controller;

import chocoteamteam.togather.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @Operation(
        summary = "이미지 저장 api", description = "이미지를 업로드하고 해당 이미지로 접근할 수 있는 Url 응답",
        tags = {"Image"}
    )
    @PostMapping
    public ResponseEntity<String> upload(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(imageService.upload(file));
    }

}
