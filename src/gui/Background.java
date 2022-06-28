package gui;

import main.codegen.CodeGenerator;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

class Background {


    private final GUI_sketch gui_sketch;
    ArrayList<Pin> pins;
    ArrayList<Connection> connections;

    ArrayList<ExpressionBlock> expressionBlock;

    Background(GUI_sketch gui_sketch) {
        this.gui_sketch = gui_sketch;

        pins = new ArrayList<Pin>();
        connections = new ArrayList<Connection>();
        expressionBlock = new ArrayList<>();

//        Initialize inputs according to enums in CodeGenerator class
        var inputs = Arrays.stream(Arrays.stream(CodeGenerator.AnalogInput.values()).map(Enum::name).toArray(String[]::new)).toList();
        for (int i = 0; i < inputs.size(); i++) {
            pins.add(new InputPin(gui_sketch, inputs.get(i), new PVector(75, (i + 1) * 200)));
        }

//        Initialize inputs according to enums in CodeGenerator class
        var outputs = Arrays.stream(Arrays.stream(CodeGenerator.DigitalOutput.values()).map(Enum::name).toArray(String[]::new)).toList();
        for (int i = 0; i < outputs.size(); i++) {
            pins.add(new OutputPin(gui_sketch, outputs.get(i), new PVector(720, (i + 1) * 200)));
        }

        expressionBlock.add(new ExpressionBlock(gui_sketch, new PVector(300, 50)));
        expressionBlock.add(new ExpressionBlock(gui_sketch, new PVector(300, 400)));

    }

    public void update(PVector mousePos) {
        for (Pin pin : pins) {
            pin.update(mousePos);
            if (pin.menu.isActive) {
                gui_sketch.menuState = true;
                gui_sketch.connecting = false;
                pin.locked = false;
                pin.menu.show();
                for (ExpressionBlock block : expressionBlock) {
                    block.hide();
                }
            } else {
                pin.menu.hide();
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

        for (ExpressionBlock block : expressionBlock) {
            block.display();
            block.show();
        }
    }

    public void hide() {
        for (Pin pin : pins) {
            pin.hide();
        }
    }
}
