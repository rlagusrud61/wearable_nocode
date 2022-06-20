package gui;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

class ExpressionBlock {


    private final GUI_sketch gui_sketch;
    PVector position;
    PVector inputConnectPos, outputConnectPos;

    final static int SIZE = 200;
    final static int CONNECTSIZE = 20;
    PShape shape, expressionBox, inputConnect, outputConnect;
    main.ExpressionNode node;

    ArrayList<InputPin> connectedInputs;
    ArrayList<OutputPin> connectedOutputs;
    ArrayList<EditableNumberBox> inputValues;
    ArrayList<EditableNumberBox> outputValues;
    ArrayList<EditableNumberBox> elses;
    ArrayList<Operator> operators;

    EditableNumberBox delay;



    ExpressionBlock(GUI_sketch gui_sketch, main.ExpressionNode node, PVector position) {
        this.gui_sketch = gui_sketch;
        this.node = node;
        this.position = position;
        this.connectedInputs = new ArrayList<>();
        this.connectedOutputs = new ArrayList<>();
        this.inputValues = new ArrayList<>();
        this.outputValues = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.elses = new ArrayList<>();

        this.inputConnectPos = new PVector(position.x - 20, position.y + SIZE / 2);
        this.outputConnectPos = new PVector(position.x + SIZE, position.y + SIZE / 2);

        this.delay = new EditableNumberBox(new PVector(position.x + 80, position.y + SIZE * 2 - 30));


        setShapes();
    }

    public void setShapes() {

        shape = new PShape(PConstants.GROUP);
        expressionBox = gui_sketch.createShape(PConstants.RECT, position.x, position.y, SIZE, SIZE * 2);
        inputConnect = gui_sketch.createShape(PConstants.RECT, inputConnectPos.x, inputConnectPos.y, CONNECTSIZE, CONNECTSIZE);
        outputConnect = gui_sketch.createShape(PConstants.RECT, outputConnectPos.x, outputConnectPos.y, CONNECTSIZE, CONNECTSIZE);

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

    public void show() {
        for (EditableNumberBox expression : inputValues) {
            expression.show();
        }

        for (EditableNumberBox expression : outputValues) {
            expression.show();
        }
        for (Choices operator : operators) {
            operator.show();
        }
    }

    public void hide() {

        for (EditableNumberBox expression : inputValues) {
            expression.hide();
        }
        for (EditableNumberBox expression : outputValues) {
            expression.hide();
        }
        for (Choices operator : operators) {
            operator.hide();
        }
    }

    public void display() {
        gui_sketch.shape(shape);
        gui_sketch.push();
        gui_sketch.fill(0);
        gui_sketch.text("Expression", position.x + 10, position.y + 20);
        gui_sketch.text("IF ", position.x + 10, position.y + 60);

        if (connectedInputs != null && connectedOutputs != null) {
            PVector spacing = new PVector(30, 60);
            int count = 0;
            for (InputPin connectedInput : connectedInputs) {
                count++;
                gui_sketch.text("Value of " + connectedInput.pinNum + " ", position.x + spacing.x, position.y + spacing.y * count);
                if (connectedInputs.size()  > 1){
                   gui_sketch.text( "AND ", position.x + spacing.x , position.y + spacing.y * count + 20);
                }
            }
//            Line between connected inputs and outputs
//            gui_sketch.line(position.x + spacing.x, position.y + spacing.y * 2 * connectedInputs.size(),  position.x + spacing.x + 100, position.y + spacing.y * 2 *  connectedInputs.size());

            for (OutputPin connectedOutput : connectedOutputs) {
                count++;
                gui_sketch.text("Value of " + connectedOutput.pinNum + " = ", position.x + spacing.x, position.y + spacing.y * count);
                gui_sketch.text("ELSE = ", position.x + spacing.x, position.y + spacing.y * count  + 20 );
            }
        }

        gui_sketch.text("DELAY ", position.x + 10, position.y + SIZE * 2 - 20);

        gui_sketch.pop();
    }


    public void addConnection(Pin pin) {
        int count = connectedInputs.size() + connectedOutputs.size();
        if (pin instanceof InputPin) {
            connectedInputs.add((InputPin) pin);
            operators.add(new Operator(pin, new PVector(position.x + SIZE / 2 + 20, position.y + 45 + (60 * count))));
            inputValues.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + SIZE / 2 + 55, position.y + 45 + (60 * count))));
        } else if (pin instanceof OutputPin) {
            connectedOutputs.add((OutputPin) pin);
            outputValues.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + SIZE / 2 + 55, position.y + 45 + (60 * count))));
            elses.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + SIZE / 2 + 55, position.y + 65 + (60 * count))));
        }
    }

//    public void addOutputConnection(OutputPin pin) {
//        connectedOutputs.add(pin);
//        outputValues.add(new EditableNumberBox(pin.pinNum, new PVector(position.x + size / 2 + 55, position.y + 45 +(120 * connectedOutputs.indexOf(pin)))));
//
//    }
}
