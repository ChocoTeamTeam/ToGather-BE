package chocoteamteam.togather.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.TimeZone;

@Configuration
public class TimeConfig {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}