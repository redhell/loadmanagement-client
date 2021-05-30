package de.bublitz.Components;

import de.bublitz.Config.BalancerConfig;
import de.bublitz.Config.ChargeboxConfig;
import de.bublitz.Serial.SerialReader;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Log4j2
public class TransferService {

    @Autowired
    private SerialReader serialReader;
    private final ChargeboxConfig chargeboxConfig;
    private final String host;
    private RestTemplate restTemplate;

    private final BalancerConfig balancerConfig;

    @Autowired
    public TransferService(ChargeboxConfig chargeboxConfig, BalancerConfig balancerConfig) {
        this.chargeboxConfig = chargeboxConfig;
        this.balancerConfig = balancerConfig;
        host = "http://" + balancerConfig.getIp() + ":" + balancerConfig.getPort();
    }

    @PostConstruct
    public void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        this.restTemplate = restTemplateBuilder.build();
        registerChargebox(chargeboxConfig.getName(), chargeboxConfig.getEvseid(), chargeboxConfig.getStarturl(), chargeboxConfig.getStopurl(), chargeboxConfig.getEmaid());
    }

    @Scheduled(fixedDelay = 15000, initialDelay = 10000)
    public void sendData() {
        log.info(serialReader.getDataMap().size());
        String url = "/load/" + chargeboxConfig.getName() + "/rawPoints";
        HttpEntity<Map<LocalDateTime, String>> entity = new HttpEntity<>(serialReader.getDataMap(), new HttpHeaders());
        ResponseEntity<Boolean> response = restTemplate.postForEntity(host + url, entity, Boolean.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            serialReader.getDataMap().clear();
        } else {
            log.warn("Fehler beim senden!");
        }
    }

    public void registerChargebox(String name, String evseid, String startURL, String stopURL, String emaid) {
        String url = "/chargebox/add?name={name}&evseid={evseid}&startURL={startURL}&stopURL={stopURL}&emaid={emaid}";
        ResponseEntity<Boolean> response = restTemplate.getForEntity(host + url, Boolean.class, name, evseid, startURL, stopURL, emaid);
        log.info(response.getStatusCode());
    }
}
