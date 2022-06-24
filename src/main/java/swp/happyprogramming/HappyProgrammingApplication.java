package swp.happyprogramming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class HappyProgrammingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyProgrammingApplication.class, args);
    }

    @Configuration
    @ComponentScan(lazyInit = true)
    static class LocalConfig {
    }

}