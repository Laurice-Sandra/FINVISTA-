package esprit.tn.flexifin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlexiFinApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlexiFinApplication.class, args);
    }

}
