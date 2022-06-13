package gui;

import main.Node;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

class ExpressionNode {


    private final GUI_sketch gui_sketch;
    PVector position;
    PVector inputConnectPos, outputConnectPos;
    EditableNumberBox edNumBox;

    int size;
    int connectSize;
    PShape shape, expressionBox, inputConnect, outputConnect;
    main.ExpressionNode node;

    ArrayList<InputPin> connectedInputs;


    ExpressionNode(GUI_sketch gui_sketch, main.ExpressionNode node, PVector position) {
        this.gui_sketch = gui_sketch;
        this.node = node;
        this.position = position;
        this.size = 200;
        this.connectSize = 20;
        this.connectedInputs = new ArrayList<>();

        this.inputConnectPos = new PVector(position.x - 20, position.y + size / 2);
        this.outputConnectPos = new PVector(position.x + size, position.y + size / 2);

        edNumBox = new EditableNumberBox(new PVector(position.x +  size / 2 + 50, position.y + 45));

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
        gui_sketch.text("IF ", position.x + 10, position.y + 60);

        if (connectedInputs != null) {
            int count = 0;
            for (InputPin connectedInput : connectedInputs) {
                count ++ ;
                gui_sketch.text(" Value of " + connectedInput.pinNum + " > ", position.x + 30, position.y + 60 * count);
            }
        }

        gui_sketch.pop();
    }

    public void addInputConnection(InputPin pin) {
        PApplet.println("Added pin " + pin.pinNum);
        connectedInputs.add(pin);
    }
}
