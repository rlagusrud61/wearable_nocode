package gui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

class Connection {

    private final GUI_sketch gui_sketch;
    Pin pin1;
    Pin pin2;
    ExpressionNode exp;

    PVector position;
    int size;
    PShape configMenu;

    Connection(GUI_sketch gui_sketch, Pin pin1, Pin pin2) {
        this.gui_sketch = gui_sketch;
        PApplet.println("new connection with " + pin1.pinNum + " with " + pin2.pinNum);

        this.pin1 = pin1;
        this.pin2 = pin2;
        size = 400;
        position = new PVector(gui_sketch.width / 2, gui_sketch.height / 2);
        configMenu = gui_sketch.createShape(PConstants.RECT, position.x, position.y, size, size);
        //openMenu();
    }

    Connection(GUI_sketch gui_sketch, Pin pin, ExpressionNode expressionNode) {
        this.gui_sketch = gui_sketch;

        PApplet.println("new connection with " + pin.pinNum + " with " + expressionNode);
        this.pin1 = pin;
        this.exp = expressionNode;



    }

    public void display() {
        gui_sketch.push();
        gui_sketch.stroke(0);
        gui_sketch.strokeWeight(2);
        if (pin2 != null) {
            gui_sketch.line(pin1.connectionPos.x, pin1.connectionPos.y, pin2.connectionPos.x, pin2.connectionPos.y);
        } else if (exp != null && pin1 instanceof InputPin) {
            gui_sketch.line(pin1.connectionPos.x, pin1.connectionPos.y, exp.inputConnectPos.x, exp.inputConnectPos.y);
        } else if (exp != null && pin1 instanceof OutputPin) {
            gui_sketch.line(pin1.connectionPos.x, pin1.connectionPos.y, exp.outputConnectPos.x, exp.outputConnectPos.y);
        }
        gui_sketch.pop();
    }

    public void openMenu() {
        configMenu.setVisible(true);
    }
}
