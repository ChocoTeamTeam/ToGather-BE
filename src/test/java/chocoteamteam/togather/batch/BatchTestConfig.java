package chocoteamteam.togather.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@EntityScan({"chocoteamteam.togather.entity", "chocoteamteam.togather.batch.domain.entity"})
@EnableJpaRepositories({"chocoteamteam.togather.repository", "chocoteamteam.togather.batch.domain.repository"})
@EnableTransactionManagement
public class BatchTestConfig {
}