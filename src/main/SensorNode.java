package main;


public class SensorNode extends IONode {


    SensorNode(String port) {
        super(port);
    }

    public enum Sensors {
        TOUCH, MICROPHONE, GSR, BENDING, DISTANCE, HR, ACCELEROMETER
    }


}
