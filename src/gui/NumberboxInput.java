package gui;

import controlP5.Numberbox;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;

// input handler for a Numberbox that allows the user to
// key in numbers with the keyboard to change the value of the numberbox
public class NumberboxInput {

    private final GUI_sketch gui_sketch;
    String text = "";

    Numberbox n;

    boolean active;


    NumberboxInput(GUI_sketch gui_sketch, Numberbox theNumberbox) {
        this.gui_sketch = gui_sketch;
        n = theNumberbox;
        gui_sketch.registerMethod("keyEvent", this);
    }

    public void keyEvent(KeyEvent k) {
        // only process key event if input is active
        if (k.getAction() == KeyEvent.PRESS && active) {
            if (k.getKey() == '\n') { // confirm input with enter
                submit();
                return;
            } else if (k.getKeyCode() == PConstants.BACKSPACE) {
                text = text.isEmpty() ? "" : text.substring(0, text.length() - 1);
                //text = ""; // clear all text with backspace
            } else if (k.getKey() < 255) {
                // check if the input is a valid (decimal) number
                final String regex = "\\d+([.]\\d{0,2})?";
                String s = text + k.getKey();
                if (java.util.regex.Pattern.matches(regex, s)) {
                    text += k.getKey();
                }
            }
            n.getValueLabel().setText(this.text);
        }
    }

    public void setActive(boolean b) {
        active = b;
        if (active) {
            n.getValueLabel().setText("");
            text = "";
        }
    }

    public void submit() {
        if (!text.isEmpty()) {
            n.setValue(PApplet.parseFloat(text));
            text = "";
        } else {
            n.getValueLabel().setText("" + n.getValue());
        }
    }

}
