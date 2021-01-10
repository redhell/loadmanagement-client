package de.bublitz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadmanagementClient {

    public static void main(String[] args) {
        SpringApplication.run(LoadmanagementClient.class, args);
        SerialReader serialReader = new SerialReader();
        serialReader.readSerial();
    }
}
