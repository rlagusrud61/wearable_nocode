package gui;

import main.*;
import main.codegen.CodeGenerator;
import processing.core.*;

import java.util.ArrayList;
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

        int sensorNodeCount = 0;
        int actuatorNodeCount = 0;
        int expressionNodeCount = 0;
        for (Node node : dataStructure.nodes) {
            if (node instanceof SensorNode) {
                sensorNodeCount++;
                background.pins.add(new InputPin(this, (SensorNode) node, new PVector(75, sensorNodeCount * 200)));
            } else if (node instanceof ActuatorNode) {
                actuatorNodeCount++;
                background.pins.add(new OutputPin(this, (ActuatorNode) node, new PVector(720, actuatorNodeCount * 200)));
            } else if (node instanceof main.ExpressionNode) {
                // should be able to handle more expressionblocks
                background.expressionBlock = new ExpressionBlock(this, (main.ExpressionNode) node, new PVector(300, 150));
            }
        }

    }


    public void draw() {
        background(225);
        PVector mousePos = new PVector(mouseX, mouseY);
        background.display();
        background.update(mousePos);

        push();
        fill(0, 255, 0);
        rect(750, 0, 50, 50);
        pop();
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

            ArrayList<CodeGenerator.Statement> statements = new ArrayList<>();

            for (OutputPin outputPin : outputPins) {
                // set Label to the Node (for code generation)

                CodeGenerator.DigitalOutput digitalOutput = CodeGenerator.DigitalOutput.valueOf(outputPin.pinNum);

                ExpressionBlock exp = outputPin.connectedExpression;

                if (exp != null) { // if there is connected expression

                    var delay = new CodeGenerator.DoubleLiteral(exp.delay.getValue()); // get delay value
                    CodeGenerator.ActuatorType type = CodeGenerator.ActuatorType.valueOf(outputPin.selected);

                    if (exp.connectedInputs != null) {
                        for (int i = 0; i < exp.connectedInputs.size() ; i ++) {

                            CodeGenerator.DoubleComparisonExpression dce = CodeGenerator.generateDoubleComparisonExpression(
                                    exp.operators.get(i).getValue(), /* Comparison operator*/
                                    exp.connectedInputs.get(i).pinNum, /* AnalogInputStatement */
                                    exp.connectedInputs.get(i).selected, /* SensorType */
                                    exp.inputValues.get(i).getValue(), /* input value to be compared*/
                                    exp.outputValues.get(i).getValue(), /* if true */
                                    exp.elses.get(i).getValue()); /* if false */

                            /* Add a new statement */
                            statements.add(new CodeGenerator.DigitalOutputStatement(digitalOutput, type, delay, dce));
                        }
                    }
                }
            }

            CodeGenerator.Program program = new CodeGenerator.Program(
                    10, statements.toArray(new CodeGenerator.Statement[0])
            );
            generateCode(program);
            exit();
        }

    }


    public void generateCode(CodeGenerator.Program program) {
        var visitor = new CodeGenerator.CodeGeneratorVisitor();
        program.accept(visitor);
        PApplet.println(visitor.getResult());
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
