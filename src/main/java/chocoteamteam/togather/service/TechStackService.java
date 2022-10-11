package chocoteamteam.togather.service;

import chocoteamteam.togather.component.aws.S3FileUtil;
import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.UpdateTechStackForm;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.repository.TechStackRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_TECH_STACK;

@RequiredArgsConstructor
@Service
public class TechStackService {
    private final TechStackRepository techStackRepository;
    private final S3FileUtil s3FileUtil;

    @Transactional
    public TechStackDto createTechStack(CreateTechStackForm form) {
        return TechStackDto.from(techStackRepository.save(TechStack.builder()
                .name(form.getName())
                .category(form.getCategory())
                .image(form.getImage())
                .build()));
    }

    public List<TechStackDto> getTechStacks() {
        return techStackRepository.findAll().stream()
                .map(TechStackDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TechStackDto updateTechStack(Long techStackId, UpdateTechStackForm form) {
        TechStack techStack = techStackRepository.findById(techStackId)
                .orElseThrow(() -> new TechStackException(NOT_FOUND_TECH_STACK));
        techStack.update(form);
        return TechStackDto.from(techStack);
    }

    @Transactional
    public TechStackDto deleteTechStack(Long techStackId) {
        TechStack techStack = techStackRepository.findById(techStackId)
                .orElseThrow(() -> new TechStackException(NOT_FOUND_TECH_STACK));
        techStackRepository.deleteById(techStack.getId());
        return TechStackDto.from(techStack);
    }

    public String imageUpload(MultipartFile file) {
        try {
            return s3FileUtil.upload(file);
        } catch (IOException e) {
            throw new TechStackException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }
}
