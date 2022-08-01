package gui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

abstract class Pin {

    String selected;
    final GUI_sketch gui_sketch;
     PShape shape, inputBox, connectionBox1, connectionBox2, connectionBox3;

    String pinNum;
    PVector position;
    PVector offset;
    static final int SIZE = 150;
    boolean mouseOverComponent;
    boolean locked; // for dragging
    Menu menu;


    PVector connectionPos;
    boolean mouseOverConnection;

    boolean clickable;

    Pin connectedPin;
    ExpressionBlock connectedExpression;

    Pin(GUI_sketch gui_sketch, String pinNum, PVector position) {
        this.gui_sketch = gui_sketch;
        this.clickable = true;
        this.pinNum = pinNum;
        this.position = position.copy();
        this.menu = new Menu(gui_sketch, this);
        this.connectionPos = new PVector(0, 0);


        // Shapes
    }


    public void setShapes() {
        gui_sketch.push();
        gui_sketch.rectMode(PConstants.CENTER);
        shape = gui_sketch.createShape(PConstants.GROUP);
        inputBox = gui_sketch.createShape(PConstants.RECT, position.x, position.y, SIZE, SIZE);
        inputBox.setFill(gui_sketch.color(200));

//        Rendering the three boxes for variables
        connectionBox1 = gui_sketch.createShape(PConstants.RECT, connectionPos.x, connectionPos.y, 20, 20);
        connectionBox2 = gui_sketch.createShape(PConstants.RECT, connectionPos.x, connectionPos.y + 30, 20, 20);
        connectionBox3 = gui_sketch.createShape(PConstants.RECT, connectionPos.x, connectionPos.y + 60, 20, 20);
//        connectionBox.setFill(gui_sketch.color(220));
        shape.addChild(inputBox);
        shape.addChild(connectionBox1);
        shape.addChild(connectionBox2);
        shape.addChild(connectionBox3);

        gui_sketch.pop();
    }


    public void update(PVector mousePos) {
        if (mousePos.x > position.x - (SIZE / 2) && mousePos.x < position.x + (SIZE / 2) &&
                mousePos.y > position.y - (SIZE / 2) && mousePos.y < position.y + (SIZE / 2) && clickable) {
            mouseOverComponent = true;
            if (!locked) {
                gui_sketch.stroke(255);
                gui_sketch.fill(153);
            }
        } else {
            gui_sketch.stroke(153);
            gui_sketch.fill(153);
            mouseOverComponent = false;
        }

        if (mousePos.x > connectionPos.x - (SIZE / 4) && mousePos.x < connectionPos.x + (SIZE / 4) &&
                mousePos.y > connectionPos.y - (SIZE / 4) && mousePos.y < connectionPos.y + (SIZE / 4) && clickable) {
            mouseOverConnection = true;
        } else {
            mouseOverConnection = false;
        }
    }


    public void mousePress() {
        if (mouseOverComponent && gui_sketch.mousePressed || locked) {
            locked = true;
            menu.show();
            gui_sketch.menuState = true;
            PApplet.println("input PIN" + pinNum);
        } else {
            locked = false;
            menu.hide();
            gui_sketch.menuState = false;
        }

        if (mouseOverConnection) {
            locked = true;
            PApplet.println("clickedconnection " + pinNum);
        } else {
            locked = false;
        }
    }

    public void mouseDrag(ExpressionBlock expressionBlock, ArrayList<Pin> otherPins, PVector mousePos) {

        if (locked) {
            offset = mousePos;
            gui_sketch.push();
            gui_sketch.stroke(15);
            gui_sketch.line(connectionPos.x, connectionPos.y, offset.x, offset.y);
            gui_sketch.pop();

            for (Pin otherPin : otherPins) {
                if (mousePos.x > otherPin.position.x - (SIZE / 2) && mousePos.x < otherPin.position.x + (SIZE / 2) &&
                        mousePos.y > otherPin.position.y - (SIZE / 2) && mousePos.y < otherPin.position.y + (SIZE / 2)) {
                    gui_sketch.connecting = true;
                    connectedPin = otherPin;
                }
            }

            if (this instanceof InputPin && mousePos.x > expressionBlock.inputConnectPos.x && mousePos.x < expressionBlock.inputConnectPos.x + expressionBlock.CONNECTSIZE &&
                    mousePos.y > expressionBlock.inputConnectPos.y && mousePos.y < expressionBlock.inputConnectPos.y + expressionBlock.CONNECTSIZE) {
                gui_sketch.connecting = true;
                connectedExpression = expressionBlock;
//                PApplet.println("I'm " + this.pinNum + " connecting to " + expressionNode);
            } else if (this instanceof OutputPin && mousePos.x > expressionBlock.outputConnectPos.x && mousePos.x < expressionBlock.outputConnectPos.x + expressionBlock.CONNECTSIZE &&
                    mousePos.y > expressionBlock.outputConnectPos.y && mousePos.y < expressionBlock.outputConnectPos.y + expressionBlock.CONNECTSIZE) {
                gui_sketch.connecting = true;
                connectedExpression = expressionBlock;
//                PApplet.println("I'm " + this.pinNum + " connecting to " + expressionNode);
            }
        }
    }

    public void mouseRelease() {
        if (gui_sketch.connecting && locked) {
            if (connectedPin == null && connectedExpression != null) {
                gui_sketch.background.connections.add(new Connection(gui_sketch, this, connectedExpression));
            } else if (connectedPin != null && !connectedPin.equals(this)) { // avoiding connection to itself
                gui_sketch.background.connections.add(new Connection(gui_sketch, this, connectedPin));
            }
            gui_sketch.connecting = false;
            locked = false;
            menu.choices.hide();
        }
    }

    public void hide() {
        shape.setVisible(false);
        clickable = false;
    }


    public void display() {

        gui_sketch.shape(shape);
        gui_sketch.push();
        gui_sketch.fill(0);
        gui_sketch.textSize(15);
        if (selected != null) {
            gui_sketch.text(selected, position.x - (SIZE / 2) + 15, position.y - 50);
        }
        gui_sketch.text(pinNum, position.x + 50, position.y + 50);
        gui_sketch.pop();
    }

    public String getType() {
        return this.selected;
    }

}
