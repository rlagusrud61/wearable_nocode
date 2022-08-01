package gui;

import main.codegen.CodeGenerator;
import processing.core.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GUI_sketch extends PApplet {

    Background background;

    boolean menuState;
    boolean connecting;

    public void setup() {

        Choices.setPApplet(this);
        EditableNumberBox.setPApplet(this);
        Operator.setPApplet(this);
        background = new Background(this);
        menuState = false;


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
            File file = createFile("generatedCode");

            List<OutputPin> outputPins = background.pins.stream()
                    .filter(OutputPin.class::isInstance)
                    .map(OutputPin.class::cast).toList();

            ArrayList<CodeGenerator.Statement> statements = new ArrayList<>();

            for (OutputPin outputPin : outputPins) {
                // set Label to the Node (for code generation)

                CodeGenerator.DigitalOutput digitalOutput = CodeGenerator.DigitalOutput.valueOf(outputPin.pinNum);
                ExpressionBlock exp = outputPin.connectedExpression;

// First check if it's a simple statement
                Pin inputPin = outputPin.connectedPin;
                if (inputPin != null) {

                    CodeGenerator.ActuatorType type = CodeGenerator.ActuatorType.valueOf(outputPin.selected);
                    CodeGenerator.AnalogInputExpression expression = new CodeGenerator.AnalogInputExpression(
                            CodeGenerator.AnalogInput.valueOf(inputPin.pinNum), CodeGenerator.SensorType.valueOf(inputPin.selected));
                    CodeGenerator.SimpleExpression simpleExpression = new CodeGenerator.SimpleExpression(expression);

                    statements.add(new CodeGenerator.DigitalOutputStatement(digitalOutput, type, new CodeGenerator.DoubleLiteral(100), simpleExpression));
                }


                if (exp != null) { // if there is connected expression

                    var delay = new CodeGenerator.DoubleLiteral(exp.delay.getValue()); // get delay value
                    CodeGenerator.ActuatorType type = CodeGenerator.ActuatorType.valueOf(outputPin.selected);

                    if (exp.connectedInputs != null) {
                        for (int i = 0; i < exp.connectedInputs.size(); i++) {
                            // Get the true and false values of the corresponding DigitalOutput
                            EditableNumberBox val = exp.outputValues
                                    .stream()
                                    .filter(value -> value.pinNum.equals(outputPin.pinNum))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("No matching values!"));

                            EditableNumberBox elseVal = exp.elses
                                    .stream()
                                    .filter(value -> value.pinNum.equals(outputPin.pinNum))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("No matching else values!"));


                            CodeGenerator.DoubleComparisonExpression dce = CodeGenerator.generateDoubleComparisonExpression(
                                    exp.operators.get(i).getValue(), /* Comparison operator*/
                                    exp.connectedInputs.get(i).pinNum, /* AnalogInputStatement */
                                    exp.connectedInputs.get(i).selected, /* SensorType */
                                    exp.inputValues.get(i).getValue(), /* input value to be compared*/
                                    val.getValue(), /* if true */
                                    elseVal.getValue()); /* if false */

                            /* Add a new statement */
                            statements.add(new CodeGenerator.DigitalOutputStatement(digitalOutput, type, delay, dce));
                        }
                    }
                }
            }

            CodeGenerator.Program program = new CodeGenerator.Program(
                    10, statements.toArray(new CodeGenerator.Statement[0])
            );
            generateCode(file, program);
//            exit();
        }

    }

    public File createFile(String fileName) {
        File file = null;
        try {
            file = new File(fileName + ".ino");
            if (file.createNewFile()) {
                PApplet.println("File created: " + file.getName());
            } else {
                PApplet.println("File " + fileName + " already exists. Overwriting...");
            }
        } catch (IOException e) {
            PApplet.println("An error occurred");
            e.printStackTrace();
        }
        return file;
    }


    public void generateCode(File file, CodeGenerator.Program program) {

        var visitor = new CodeGenerator.CodeGeneratorVisitor();
        program.accept(visitor);

        FileWriter writer = null;
        try {
            writer = new FileWriter(file.getCanonicalPath(), false);
            writer.write(visitor.getResult());
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void mouseDragged() {
        PVector mousePos = new PVector(mouseX, mouseY);

        for (Pin pin : background.pins) {
            for (ExpressionBlock block : background.expressionBlock) {
                pin.mouseDrag(block, background.pins, mousePos);
            }
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
