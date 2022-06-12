package gui;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Numberbox;
import processing.core.PApplet;
import processing.core.PVector;

class EditableNumberBox {
// Editable Numberbox for ControlP5

    static GUI_sketch p;
    PVector pos;
    ControlP5 cp5;

    EditableNumberBox(PVector pos) {

        this.pos = pos;

        if (cp5 == null) {
            cp5 = new ControlP5(p);
        }
        Numberbox n = cp5.addNumberbox("numbers")
                .setSize(100, 20)
                .setPosition(pos.x, pos.y)
                .setValue(0);

        makeEditable(n);
    }

    public void show() {
        cp5.show();
    }

    public void hide() {
        cp5.hide();
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
