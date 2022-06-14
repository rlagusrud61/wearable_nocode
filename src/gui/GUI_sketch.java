package gui;

import main.*;
import main.codegen.CodeGenerator;
import processing.core.*;

import java.beans.Expression;
import java.util.List;

public class GUI_sketch extends PApplet {


    DataStructure dataStructure;
    Background background;

    boolean menuState;
    boolean connecting;

    public void setup() {

        Choices.setPApplet(this);
        EditableNumberBox.setPApplet(this);
        Operator.setPApplet(this);
        background = new Background(this);
        menuState = false;

        dataStructure = new DataStructure();

//        for (int i = 0; i < dataStructure.nodes.size(); i++) {
//            Node node = dataStructure.nodes.get(i);
//
//            if (node instanceof SensorNode) {
//                background.pins.add(new InputPin(this, node, new PVector(75, i * 100 + 200)));
//            } else if (node instanceof ActuatorNode) {
//                background.pins.add(new OutputPin(this, node, new PVector(720, i * 100 + 200)));
//            }
//        }

        int sensorNodeCount = 0;
        int actuatorNodeCount = 0;
        for (Node node : dataStructure.nodes) {
            if (node instanceof SensorNode) {
                sensorNodeCount++;
                background.pins.add(new InputPin(this, (SensorNode) node, new PVector(75, sensorNodeCount * 200)));
            } else if (node instanceof ActuatorNode) {
                actuatorNodeCount++;
                background.pins.add(new OutputPin(this, (ActuatorNode) node, new PVector(720, actuatorNodeCount * 200)));
            } else if (node instanceof main.ExpressionNode) {
                background.expressionBlock = new ExpressionBlock(this, (main.ExpressionNode) node, new PVector(300, 150));
            }
        }

    }


    public void draw() {
        background(225);


        PVector mousePos = new PVector(mouseX, mouseY);


        background.display();
        background.update(mousePos);
    }

    public void mousePressed() {
        for (Pin pin : background.pins) {
            pin.mousePress();
        }
    }

    public void mouseClicked() {
        PVector mousePos = new PVector(mouseX, mouseY);
        if (mousePos.x > 750 && mousePos.y < 50) {
            PApplet.println("DONE");

            List<OutputPin> outputPins = background.pins.stream()
                    .filter(OutputPin.class::isInstance)
                    .map(OutputPin.class::cast).toList();

            RootNode root = new RootNode();
            for (OutputPin outputPin : outputPins) {
                // set Label to the Node (for code generation)
                outputPin.node.setLabel(outputPin.selected);
                dataStructure.addConnection(root, outputPin.node);
                ExpressionBlock exp = outputPin.connectedExpression;
                if (exp != null) {
                    dataStructure.addConnection(outputPin.node, exp.node);
                    if (outputPin.connectedExpression.connectedInputs != null){
                        for (InputPin inputPin : exp.connectedInputs ){
                            inputPin.node.setLabel(inputPin.selected);
                            exp.node.addStatement(exp.operators.get(0).getValue(), inputPin.pinNum, exp.inputValues.get(0).getValue());
                            dataStructure.addConnection(exp.node, inputPin.node);

                        }
                    }
                }
            }

            generateCode();
            exit();
        }
    }

    public void generateCode() {
        CodeGenerator.generateCode(dataStructure);
    }

    public void mouseDragged() {
        PVector mousePos = new PVector(mouseX, mouseY);

        for (Pin pin : background.pins) {
            pin.mouseDrag(background.expressionBlock, background.pins, mousePos);
        }
    }

    public void mouseReleased() {
        PVector mousePos = new PVector(mouseX, mouseY);
        for (Pin pin : background.pins) {
            pin.mouseRelease();
        }

//        PApplet.println(background.connections.toString());
    }


    public void settings() {
        size(800, 800);
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"--window-color=#666666", "--stop-color=#cccccc", "GUI_sketch"};
        GUI_sketch p = new GUI_sketch();
        if (passedArgs != null) {
            PApplet.runSketch(concat(appletArgs, passedArgs), p);
        } else {
            PApplet.runSketch(appletArgs, p);
        }
    }
}
