package de.bublitz.Serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import de.bublitz.Config.SerialConfig;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Log4j2
@Component
public class SerialReader {

    @Autowired
    private SerialConfig serialConfig;

    @Getter
    private Map<LocalDateTime, String> dataMap;
    private String currentString = "";
    private SerialPort comPort;

    public SerialReader() {
        dataMap = new ConcurrentSkipListMap<>();
    }

    @PostConstruct
    public void setUp() {
        comPort = SerialPort.getCommPort(serialConfig.getPort());
        this.addListener();
    }

    public void setComPort(String comPort) {
        this.comPort = SerialPort.getCommPort(comPort);
    }

    public void addListener() {
        //SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                StringBuilder rawData = new StringBuilder();
                byte[] newData = event.getReceivedData();
                log.debug("Received data of size: " + newData.length);
                for (byte newDatum : newData) rawData.append((char) newDatum);
                collectRawData(rawData.toString());
            }
        });
    }

    @PreDestroy
    public void removeListener() {
        comPort.removeDataListener();
        comPort.closePort();
        log.warn("Shutdown Serial Port");
    }

    private synchronized void collectRawData(String rawData) {
        rawData = rawData.replaceAll("\\R+", "");
        if(rawData.contains("{")) {
            if(currentString!=null) {
                 Arrays.stream(currentString.split(";"))
                        .filter(data -> data.matches("^\\{\"I\":\"\\d+\\.\\d+\",\"unit\":\"A\"};?"))
                        .forEach(data -> {
                            dataMap.put(LocalDateTime.now(), data);
                            log.info(data);
                        });
                currentString = currentString.substring(currentString.lastIndexOf(";")+1) + rawData;
            }
        } else {
            currentString += rawData;
        }
    }
}
