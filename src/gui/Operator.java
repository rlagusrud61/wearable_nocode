package gui;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;
import java.util.List;

public class Operator extends Choices implements CallbackListener {

    Operator(Pin pin, PVector pos) {
        super(pin);


        List operators = Arrays.asList(">", "<");
        if (cp5 == null) {
            cp5 = new ControlP5(p);
        }

        ScrollableList list = cp5.addScrollableList("op"+ pin.pinNum)
                .setPosition(pos.x, pos.y)
                .setSize(30, 80)
                .setBarHeight(20)
                .setItemHeight(40)
                .addItems(operators)
                .setValue(0)
                .show();


        cp5.getController("op"+pin.pinNum).addCallback(new CallbackListener() {
                                                                 public void controlEvent(CallbackEvent theEvent) {
                                                                     switch (theEvent.getAction()) {
                                                                         case (ControlP5.ACTION_RELEASE):
                                                                             PApplet.println(theEvent.getController().getLabel());
                                                                     }
                                                                 }
                                                             }
        );
    }


    @Override
    public void controlEvent(CallbackEvent callbackEvent) {

    }
}