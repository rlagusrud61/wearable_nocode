package main;

public class ActuatorNode extends Node{

    ActuatorNode(String port){
        super(port);
    }

    enum Actuators{
        BUZZER, VIBRATING_MOTOR, NEOPIXEL,SERVO
    }
}

