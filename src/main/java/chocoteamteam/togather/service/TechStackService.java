package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
