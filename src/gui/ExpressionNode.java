package gui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

class ExpressionNode {


    private final GUI_sketch gui_sketch;
    PVector position;
    PVector inputConnectPos, outputConnectPos;
    EditableNumberBox edNumBox;

    int size;
    int connectSize;
    PShape shape, expressionBox, inputConnect, outputConnect;


    ExpressionNode(GUI_sketch gui_sketch, PVector position) {
        this.gui_sketch = gui_sketch;
        this.position = position;
        this.size = 200;
        this.connectSize = 20;

        this.inputConnectPos = new PVector(position.x - 20, position.y + size / 2);
        this.outputConnectPos = new PVector(position.x + size, position.y + size / 2);

        edNumBox = new EditableNumberBox(new PVector(position.x, position.y + 50));

        setShapes();
    }

    public void setShapes() {

        shape = new PShape(PConstants.GROUP);
        expressionBox = gui_sketch.createShape(PConstants.RECT, position.x, position.y, size, size);
        inputConnect = gui_sketch.createShape(PConstants.RECT, inputConnectPos.x, inputConnectPos.y, connectSize, connectSize);
        outputConnect = gui_sketch.createShape(PConstants.RECT, outputConnectPos.x, outputConnectPos.y, connectSize, connectSize);

        shape.addChild(expressionBox);
        shape.addChild(inputConnect);
        shape.addChild(outputConnect);

    }


    public void mouseDrag(PVector mousePos) {
        PApplet.println(gui_sketch.connecting);
        if (gui_sketch.connecting &&
                mousePos.x > inputConnectPos.x && mousePos.x < inputConnectPos.x + connectSize &&
                mousePos.y > inputConnectPos.x && mousePos.y < inputConnectPos.y + connectSize) {
        }


    }
    public void mouseRelease(PVector mousePos) {

        if (gui_sketch.connecting &&
                mousePos.x > inputConnectPos.x && mousePos.x < inputConnectPos.x + connectSize &&
                mousePos.y > inputConnectPos.x && mousePos.y < inputConnectPos.y + connectSize) {
            PApplet.println("WEEEEEEEEEEEEEEEEEEEEEE");
        }


    }

    public void show() {
        edNumBox.show();
    }

    public void hide() {
        edNumBox.hide();
    }

    public void display() {

        gui_sketch.shape(shape);
        gui_sketch.push();
        gui_sketch.fill(0);
        gui_sketch.text("Expression", position.x + 10, position.y + 20);

        gui_sketch.pop();
    }
}
