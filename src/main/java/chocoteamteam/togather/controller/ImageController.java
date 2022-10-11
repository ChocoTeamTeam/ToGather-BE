package chocoteamteam.togather.controller;

import chocoteamteam.togather.service.ImageService;
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

    @PostMapping
    public ResponseEntity<String> upload(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(imageService.upload(file));
    }

}
