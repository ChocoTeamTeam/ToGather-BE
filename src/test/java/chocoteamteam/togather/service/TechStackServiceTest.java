package chocoteamteam.togather.service;

import chocoteamteam.togather.DataCleanUp;
import chocoteamteam.togather.dto.CreateTechStackForm;
import chocoteamteam.togather.dto.TechStackDto;
import chocoteamteam.togather.dto.UpdateTechStackForm;
import chocoteamteam.togather.entity.TechStack;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.TechStackException;
import chocoteamteam.togather.repository.TechStackRepository;
import chocoteamteam.togather.type.TechCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Import(DataCleanUp.class)
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

    @Test
    @DisplayName("기술 스택 수정 성공")
    void updateTechStackSuccess() {
        //given
        given(techStackRepository.findById(any()))
                .willReturn(Optional.of(new TechStack()));
        //when
        TechStackDto techStackDto = techStackService.updateTechStack(1L,
                UpdateTechStackForm.builder()
                        .category(TechCategory.FRONTEND)
                        .name("수정 이름")
                        .image("수정 이미지")
                        .build());
        //then
        assertEquals("수정 이름", techStackDto.getName());
        assertEquals(TechCategory.FRONTEND, techStackDto.getCategory());
    }

    @Test
    @DisplayName("기술 스택 수정 실패 - 해당 기술 스택 없음")
    void updateTechStack_NotFoundTechStack() {
        //given
        given(techStackRepository.findById(any()))
                .willReturn(Optional.empty());
        //when
        TechStackException exception = assertThrows(TechStackException.class,
                () -> techStackService.updateTechStack(1L,
                        UpdateTechStackForm.builder()
                                .category(TechCategory.FRONTEND)
                                .name("수정 이름")
                                .image("수정 이미지")
                                .build()));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }

    @Test
    @DisplayName("기술 스택 삭제 성공")
    void deleteTechStackSuccess() {
        //given
        given(techStackRepository.findById(any()))
                .willReturn(Optional.of(TechStack.builder().id(99L).build()));
        //when
        techStackService.deleteTechStack(9876L);
        //then
        verify(techStackRepository, times(1)).deleteById(99L);
    }

    @Test
    @DisplayName("기술 스택 삭제 실패 - 해당 기술 스택 없음")
    void deleteTechStack_NotFoundTechStack() {
        //given
        given(techStackRepository.findById(any()))
                .willReturn(Optional.empty());
        //when
        TechStackException exception = assertThrows(TechStackException.class,
                () -> techStackService.deleteTechStack(1L));
        //then
        assertEquals(ErrorCode.NOT_FOUND_TECH_STACK, exception.getErrorCode());
    }
}