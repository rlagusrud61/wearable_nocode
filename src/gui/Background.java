package gui;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

class Background {


    private final GUI_sketch gui_sketch;
    ArrayList<Pin> pins;
    ArrayList<Connection> connections;

    ExpressionNode expressionNode;

    Background(GUI_sketch gui_sketch) {
        this.gui_sketch = gui_sketch;

        pins = new ArrayList<Pin>();
        connections = new ArrayList<Connection>();

    }

    public void update(PVector mousePos) {
        for (Pin pin : pins) {
            pin.update(mousePos);
            if (pin.menu.isActive) {
                gui_sketch.menuState = true;
                gui_sketch.connecting = false;
                pin.locked = false;
                pin.menu.show();
                expressionNode.hide();
            }
        }
    }

    public void display() {
        for (Pin pin : pins) {
            pin.display();
        }

        for (Connection connection : connections) {
            connection.display();
        }
        expressionNode.display();
//        if (!gui_sketch.menuState) {
//            expressionNode.edNumBox.show();
//        }
    }

    public void hide() {
        for (Pin pin : pins) {
            pin.hide();
        }
    }
}
