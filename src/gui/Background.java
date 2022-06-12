package gui;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

class Background {


    private final GUI_sketch gui_sketch;
    ArrayList<Pin> pins;
    ArrayList<Connection> connections;

    ArrayList<ExpressionNode> expressionNodes;

    Background(GUI_sketch gui_sketch) {
        this.gui_sketch = gui_sketch;

        pins = new ArrayList<Pin>();
        connections = new ArrayList<Connection>();
        expressionNodes = new ArrayList<ExpressionNode>();

//        InputPin A0 = new InputPin(gui_sketch,"A0", new PVector(75, 200));
//        InputPin A1 = new InputPin(gui_sketch, "A1", new PVector(75, 450));
//        InputPin A2 = new InputPin(gui_sketch, "A2", new PVector(75, 700));

//        OutputPin D9 = new OutputPin(gui_sketch, "D9", new PVector(720, 200));
//        OutputPin D10 = new OutputPin(gui_sketch, "D10", new PVector(720, 450));
//        OutputPin D11 = new OutputPin(gui_sketch,"D11", new PVector(720, 700));

        ExpressionNode expressionNode = new ExpressionNode(gui_sketch, new PVector(300, 150));
        expressionNodes.add(expressionNode);


//        pins.add(A0);
//        pins.add(A1);
//        pins.add(A2);
//        pins.add(D9);
//        pins.add(D10);
//        pins.add(D11);
    }

    public void update(PVector mousePos) {
        for (Pin pin : pins) {
            pin.update(mousePos);
            if (pin.menu.isActive) {
                gui_sketch.menuState = true;
                pin.menu.show();
                for (ExpressionNode expressionNode : expressionNodes) {
                    expressionNode.hide();
                }
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

        for (ExpressionNode expressionNode : expressionNodes){
            expressionNode.display();
            if (!gui_sketch.menuState) {
                expressionNode.edNumBox.show();
            }
        }
    }

    public void hide() {
        for (Pin pin : pins) {
            pin.hide();
        }
    }
}
