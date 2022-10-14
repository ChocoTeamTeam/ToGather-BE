package chocoteamteam.togather.batch.domain.repository;

import chocoteamteam.togather.batch.domain.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
