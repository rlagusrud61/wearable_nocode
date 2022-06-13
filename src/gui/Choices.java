package gui;

import controlP5.*;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Choices implements ControlListener {

    Pin pin;
    ControlP5 cp5;

    static PApplet p;
    Choices(Pin pin) {

        this.pin = pin;

        List sensors = new ArrayList();

        if (pin instanceof InputPin) {
            sensors = Arrays.asList("bending sensor", "touch sensor", "HR sensor", "GSR");
        } else if (pin instanceof OutputPin) {
            sensors = Arrays.asList("buzzer", "vibrating motor", "Neopixel");
        }
        if (cp5 == null) {
            cp5 = new ControlP5((PApplet) p);
        }

        ScrollableList list = cp5.addScrollableList(pin.pinNum)
                .setPosition(200, 300)
                .setSize(400, 300)
                .setBarHeight(20)
                .setItemHeight(40)
                .addItems(sensors)
                .hide();


        cp5.getController(pin.pinNum).addCallback(new CallbackListener() {
                                                      public void controlEvent(CallbackEvent theEvent) {
                                                          switch (theEvent.getAction()) {
                                                              case (ControlP5.ACTION_RELEASE):
                                                                  PApplet.println(pin.selected = theEvent.getController().getLabel());
                                                                  pin.menu.show();
                                                          }
                                                      }
                                                  }
        );
    }


    public void show() {
        ScrollableList pinController = cp5.get(ScrollableList.class, pin.pinNum);
        if (pinController != null ){
            pinController.show();
        }
    }

    public void hide() {
        ScrollableList pinController = cp5.get(ScrollableList.class, pin.pinNum);
        if (pinController != null ){
            pinController.hide();
        }
    }

    public void controlEvent(ControlEvent theEvent) {
        PApplet.println("Something happened");
    }


    public static void setPApplet(PApplet sketch) {
        p = sketch;
    }
}
