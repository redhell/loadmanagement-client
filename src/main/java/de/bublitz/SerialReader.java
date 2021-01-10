package de.bublitz;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SerialReader {
    @Getter
    private List<String> dataList = new LinkedList<>();
    private String currentString ="";


    public void readSerial() {
        //SerialPort comPort = SerialPort.getCommPorts()[0];
        SerialPort comPort = SerialPort.getCommPort("/dev/cu.usbmodem142401");
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

    private synchronized void collectRawData(String rawData) {
        if(rawData.contains("{")) {
            if(currentString!=null) {
                dataList.addAll(Arrays.stream(currentString.split(";")).collect(Collectors.toList()));
                log.info(dataList.get(dataList.size()-1));
            }
            //neuer Datensatz
            currentString = currentString.substring(currentString.lastIndexOf(";")+1) + rawData;
        } else {
            currentString += rawData;
        }
    }
}
