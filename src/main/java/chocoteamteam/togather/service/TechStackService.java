package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
}
