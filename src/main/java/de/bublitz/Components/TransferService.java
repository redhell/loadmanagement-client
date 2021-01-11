package de.bublitz.Components;

import de.bublitz.Config.ConfigProperties;
import de.bublitz.Serial.SerialReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Log4j2
public class TransferService {
    @Autowired
    private SerialReader serialReader;

    @Autowired
    private ConfigProperties configProperties;


    private final RestTemplate restTemplate;

    public TransferService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        //registerChargebox(configProperties.getName(), configProperties.getEvseid(), "testURL", "stopURL");
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void sendData() {
        log.info(serialReader.getDataMap().size());
        String url = "/load/"+ configProperties.getName() + "/rawPoints";
        HttpEntity<Map<LocalDateTime, String>> entity = new HttpEntity<>(serialReader.getDataMap(), new HttpHeaders());
        ResponseEntity<Boolean> response = restTemplate.postForEntity("http://localhost:8080" + url, entity, Boolean.class);
    }

    public void registerChargebox(String name, String evseid, String startURL, String stopURL) {
        String url = "/chargebox/add?name={name}&evseid={evseid}&startURL={startURL}&stopURL={stopURL}";
        ResponseEntity<Boolean> response = restTemplate.getForEntity("http://localhost:8080" + url, Boolean.class, name, evseid, startURL, stopURL);
        log.info(response.getStatusCode());
    }
}
