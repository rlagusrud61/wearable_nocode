package gui;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

class ExpressionBlock {


    private final GUI_sketch gui_sketch;
    PVector position;
    PVector inputConnectPos, outputConnectPos;

    int size;
    int connectSize;
    PShape shape, expressionBox, inputConnect, outputConnect;
    main.ExpressionNode node;

    ArrayList<InputPin> connectedInputs;
    ArrayList<OutputPin> connectedOutputs;
    ArrayList<EditableNumberBox> inputValues;
    ArrayList<EditableNumberBox> outputValues;
    ArrayList<Operator> operators;


    ExpressionBlock(GUI_sketch gui_sketch, main.ExpressionNode node, PVector position) {
        this.gui_sketch = gui_sketch;
        this.node = node;
        this.position = position;
        this.size = 200;
        this.connectSize = 20;
        this.connectedInputs = new ArrayList<>();
        this.connectedOutputs = new ArrayList<>();
        this.inputValues = new ArrayList<>();
        this.outputValues = new ArrayList<>();
        this.operators = new ArrayList<>();

        this.inputConnectPos = new PVector(position.x - 20, position.y + size / 2);
        this.outputConnectPos = new PVector(position.x + size, position.y + size / 2);


        setShapes();
    }

    public void setShapes() {

        shape = new PShape(PConstants.GROUP);
        expressionBox = gui_sketch.createShape(PConstants.RECT, position.x, position.y, size, size * 2);
        inputConnect = gui_sketch.createShape(PConstants.RECT, inputConnectPos.x, inputConnectPos.y, connectSize, connectSize);
        outputConnect = gui_sketch.createShape(PConstants.RECT, outputConnectPos.x, outputConnectPos.y, connectSize, connectSize);

        shape.addChild(expressionBox);
        shape.addChild(inputConnect);
        shape.addChild(outputConnect);

    }


//    private void showExpressionsAndOperators() {
//        for (EditableNumberBox expression : expressions) {
//            expression.show();
//        }
//        for (Choices operator: operators){
//            operator.show();
//        }
//    }

    public void show(){
        for (EditableNumberBox expression : inputValues) {
            expression.show();
        }

        for (EditableNumberBox expression : outputValues) {
            expression.show();
        }
        for (Choices operator : operators){
            operator.show();
        }
    }

    public void hide() {

        for (EditableNumberBox expression : inputValues ) {
            expression.hide();
        }
        for (EditableNumberBox expression : outputValues ) {
            expression.hide();
        }
        for (Choices operator : operators){
            operator.hide();
        }
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
                count++;
                gui_sketch.text(" Value of " + connectedInput.pinNum + " ", position.x + 30, position.y + 60 * count);
            }
        }


        if (connectedOutputs != null) {
            int count = 0;
            for (OutputPin connectedOutput : connectedOutputs) {
                count++;
                gui_sketch.text(" Value of " + connectedOutput.pinNum + " = ", position.x + 30, position.y + 120 * count);
            }
        }

        gui_sketch.pop();

    }


    public void addInputConnection(InputPin pin) {
        connectedInputs.add(pin);
        operators.add(new Operator(pin, new PVector(position.x + size/2 + 20, position.y + 45 + (60 * connectedInputs.indexOf(pin)))));
        inputValues.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + size / 2 + 55, position.y + 45 + (60 * connectedInputs.indexOf(pin)))));
    }

    public void addOutputConnection(OutputPin pin) {
        connectedOutputs.add(pin);
        outputValues.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + size / 2 + 55, position.y + 100 +(120 * connectedOutputs.indexOf(pin)))));

    }
}
