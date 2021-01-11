package de.bublitz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoadmanagementClient {

    public static void main(String[] args) {
        SpringApplication.run(LoadmanagementClient.class, args);
        //SerialReader serialReader = new SerialReader();
        //serialReader.addListener();
    }
}
