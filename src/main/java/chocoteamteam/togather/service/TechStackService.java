package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.UpdateTechStackForm;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static chocoteamteam.togather.exception.ErrorCode.NOT_FOUND_TECH_STACK;

@RequiredArgsConstructor
@Service
public class TechStackService {
    private final TechStackRepository techStackRepository;

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

}
