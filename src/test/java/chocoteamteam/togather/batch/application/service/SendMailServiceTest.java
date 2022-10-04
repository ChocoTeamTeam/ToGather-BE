package chocoteamteam.togather.batch.application.service;

import chocoteamteam.togather.batch.domain.entity.Mail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SendMailServiceTest {
    @Autowired
    SendMailService sendMailService;

    @Test
    @DisplayName("메일 전송 성공")
    void sendMail() {
        //given
        Mail mail = Mail.builder()
                .id(1L)
                .email("iiineong@gmail.com")
                .subject("테스트 메일 제목")
                .content("<h1>메일 내용</h1>")
                .build();
        //when
        boolean result = sendMailService.sendMail(mail);
        //then
        assertThat(result).isTrue();
    }
}