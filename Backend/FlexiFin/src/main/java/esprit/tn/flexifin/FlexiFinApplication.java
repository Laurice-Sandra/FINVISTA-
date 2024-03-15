package esprit.tn.flexifin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlexiFinApplication {

    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.trustStore", "C:\\Program Files\\Java\\jdk-17\\lib\\security\\cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");


        SpringApplication.run(FlexiFinApplication.class, args);
    }

}
