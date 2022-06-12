package gui;

import processing.core.PShape;
import processing.core.PVector;

abstract class ClickableObject {


    private final GUI_sketch gui_sketch;
    PShape shape;

    boolean mouseOverComponent;
    boolean locked; // for dragging

    PVector position;
    PVector offset;
    int size = 0;

    ClickableObject(GUI_sketch gui_sketch) {
        this.gui_sketch = gui_sketch;
        offset = new PVector(0, 0);
    }

    public void display() {
        if (!gui_sketch.menuState) {
            shape.setVisible(false);
        }
    }

    public void update(PVector mousePos) {

        if (mousePos.x > position.x - size && mousePos.x < position.x + size &&
                mousePos.y > position.y - size && mousePos.y < position.y + size) {
            mouseOverComponent = true;
            if (!locked) {
                gui_sketch.stroke(255);
                gui_sketch.fill(153);
            }
        } else {
            gui_sketch.stroke(153);
            gui_sketch.fill(153);
            mouseOverComponent = false;
        }
    }

    public void mousePress(PVector mousePos) {
        if (mouseOverComponent) {
            locked = true;
            gui_sketch.fill(255, 255, 255);
        } else {
            locked = false;
        }

        offset = mousePos.copy().sub(position);
    }

    public void mouseDrag(PVector mousePos) {
        if (locked) {
            this.position = mousePos.copy().sub(offset);
        }
    }


    public void mouseRelease() {
        locked = false;
    }
}
