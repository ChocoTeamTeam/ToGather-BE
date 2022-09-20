package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.repository.TechStackRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TechStackServiceTest {

    @Mock
    private TechStackRepository techStackRepository;

    @InjectMocks
    private TechStackService techStackService;


    @Test
    @DisplayName("기술 스택 등록 성공")
    void createTechStackSuccess() {
        //given
        TechStack techStack = TechStack.builder()
                .id(1L)
                .name("java")
                .image("java_img")
                .build();

        given(techStackRepository.save(any()))
                .willReturn(techStack);
        ArgumentCaptor<TechStack> captor = ArgumentCaptor.forClass(TechStack.class);

        //when
        TechStackDto techStackDto =
                techStackService.createTechStack(new CreateTechStackForm());
        //then
        assertEquals(techStack.getId(), techStackDto.getId());
        assertEquals(techStack.getName(), techStackDto.getName());
        verify(techStackRepository, times(1)).save(captor.capture());
    }

}