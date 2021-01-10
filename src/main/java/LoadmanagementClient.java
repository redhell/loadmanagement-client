import Serial.SerialReader;

public class LoadmanagementClient {

    public static void main(String[] args) {
        SerialReader serialReader = new SerialReader();
        serialReader.readSerial();
    }
}
