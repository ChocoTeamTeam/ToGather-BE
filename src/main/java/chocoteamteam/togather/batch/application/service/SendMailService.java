package chocoteamteam.togather.batch.application.service;

import chocoteamteam.togather.batch.domain.entity.Mail;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.MailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailService {
    private final JavaMailSender javaMailSender;
    private final static String FROM_NAME = "ToGather";
    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

    public boolean sendMail(Mail mail) {
        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(FROM_EMAIL, FROM_NAME);
            mimeMessageHelper.setTo(mail.getEmail());
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getContent(), true);
        };

        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            throw new MailException(ErrorCode.EMAIL_SEND_FAIL);
        }
        return true;
    }
}
