package gui;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Objects;

class EditableNumberBox {
// Editable Numberbox for ControlP5

    static GUI_sketch p;
    String pinNum;
    PVector pos;
    ControlP5 cp5;

    EditableNumberBox(PVector pos) {

        this.pos = pos;

        if (cp5 == null) {
            cp5 = new ControlP5(p);
        }
        Numberbox n = cp5.addNumberbox("delay")
                .setSize(30, 20)
                .setPosition(pos.x, pos.y)
                .setValue(0)
                .setDecimalPrecision(0);
        makeEditable(n);

        cp5.getController("delay").addCallback(new CallbackListener() {
                                                   public void controlEvent(CallbackEvent theEvent) {
                                                       switch (theEvent.getAction()) {
                                                           case (ControlP5.ACTION_RELEASE):
                                                               PApplet.println("delay:  " + theEvent.getController().getValue());
                                                       }
                                                   }
                                               }
        );
    }

    EditableNumberBox(String pinNum, PVector pos) {

        this.pos = pos;
        this.pinNum = pinNum;

        if (cp5 == null) {
            cp5 = new ControlP5(p);
        }
        Numberbox n = cp5.addNumberbox(pinNum)
                .setSize(30, 20)
                .setPosition(pos.x, pos.y)
                .setValue(0)
                .setDecimalPrecision(0);
        makeEditable(n);

        cp5.getController(pinNum).addCallback(new CallbackListener() {
                                                  public void controlEvent(CallbackEvent theEvent) {
                                                      switch (theEvent.getAction()) {
                                                          case (ControlP5.ACTION_RELEASE):
                                                              PApplet.println(pinNum + " " + theEvent.getController().getValue());
                                                      }
                                                  }
                                              }
        );
    }

    public int getValue() {
        return (int) cp5.getController(Objects.requireNonNullElse(pinNum, "delay")).getValue();
    }

    public void show() {
        cp5.getController(Objects.requireNonNullElse(pinNum, "delay")).show();
    }

    public void hide() {

        cp5.getController(Objects.requireNonNullElse(pinNum, "delay")).hide();
    }


    // function that will be called when controller 'numbers' changes
    public void numbers(int f) {
        PApplet.println("received " + f + " from Numberbox numbers ");
    }


    public void makeEditable(Numberbox n) {
        // allows the user to click a numberbox and type in a number which is confirmed with RETURN


        final NumberboxInput nin = new NumberboxInput(p, n); // custom input handler for the numberbox

        // control the active-status of the input handler when releasing the mouse button inside
        // the numberbox. deactivate input handler when mouse leaves.
        n.onClick(new CallbackListener() {
                      public void controlEvent(CallbackEvent theEvent) {
                          nin.setActive(true);
                      }
                  }
        ).onLeave(new CallbackListener() {
                      public void controlEvent(CallbackEvent theEvent) {
                          nin.setActive(false);
                          nin.submit();
                      }
                  }
        );
    }

    public static void setPApplet(GUI_sketch sketch) {
        p = sketch;
    }
}
