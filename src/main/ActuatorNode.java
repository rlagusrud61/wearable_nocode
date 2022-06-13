package main;

public class ActuatorNode extends IONode{

    ActuatorNode(String port){
        super(port);
    }

    enum Actuators{
        BUZZER, VIBRATING_MOTOR, NEOPIXEL,SERVO
    }
}

