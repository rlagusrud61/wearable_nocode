package gui;

import main.IONode;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

abstract class Pin {

    String selected;
    private final GUI_sketch gui_sketch;
    PShape shape, inputBox, connectionBox;

    String pinNum;
    PVector position;
    PVector offset;
    int size;
    boolean mouseOverComponent;
    boolean locked; // for dragging
    Menu menu;

    IONode node;

    PVector connectionPos;
    boolean mouseOverConnection;

    boolean clickable;

    Pin connectedPin;
    ExpressionBlock connectedExpression;

    Pin(GUI_sketch gui_sketch, IONode node, PVector position) {
        this.gui_sketch = gui_sketch;
        this.node = node;
        this.clickable = true;
        this.pinNum = node.port;
        this.position = position.copy();
        this.size = 150;
        this.menu = new Menu(gui_sketch, this);
        this.connectionPos = new PVector(0, 0);


        // Shapes
    }


    public void setShapes() {
        gui_sketch.push();
        gui_sketch.rectMode(PConstants.CENTER);
        shape = gui_sketch.createShape(PConstants.GROUP);
        inputBox = gui_sketch.createShape(PConstants.RECT, position.x, position.y, size, size);
        inputBox.setFill(gui_sketch.color(200));

        connectionBox = gui_sketch.createShape(PConstants.RECT, connectionPos.x, connectionPos.y, 20, 20);
        connectionBox.setFill(gui_sketch.color(220));
        shape.addChild(inputBox);
        shape.addChild(connectionBox);

        gui_sketch.pop();
    }


    public void update(PVector mousePos) {
        if (mousePos.x > position.x - (size / 2) && mousePos.x < position.x + (size / 2) &&
                mousePos.y > position.y - (size / 2) && mousePos.y < position.y + (size / 2) && clickable) {
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

        if (mousePos.x > connectionPos.x - (size / 4) && mousePos.x < connectionPos.x + (size / 4) &&
                mousePos.y > connectionPos.y - (size / 4) && mousePos.y < connectionPos.y + (size / 4) && clickable) {
            mouseOverConnection = true;
        } else {
            mouseOverConnection = false;
        }
    }


    public void mousePress() {
        if (mouseOverComponent) {
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
                if (mousePos.x > otherPin.position.x - (size / 2) && mousePos.x < otherPin.position.x + (size / 2) &&
                        mousePos.y > otherPin.position.y - (size / 2) && mousePos.y < otherPin.position.y + (size / 2)) {
                    gui_sketch.connecting = true;
                    connectedPin = otherPin;
                }
            }

            if (this instanceof InputPin && mousePos.x > expressionBlock.inputConnectPos.x && mousePos.x < expressionBlock.inputConnectPos.x + expressionBlock.connectSize &&
                    mousePos.y > expressionBlock.inputConnectPos.y && mousePos.y < expressionBlock.inputConnectPos.y + expressionBlock.connectSize) {
                gui_sketch.connecting = true;
                connectedExpression = expressionBlock;
//                PApplet.println("I'm " + this.pinNum + " connecting to " + expressionNode);
            } else if (this instanceof OutputPin && mousePos.x > expressionBlock.outputConnectPos.x && mousePos.x < expressionBlock.outputConnectPos.x + expressionBlock.connectSize &&
                    mousePos.y > expressionBlock.outputConnectPos.y && mousePos.y < expressionBlock.outputConnectPos.y + expressionBlock.connectSize) {
                gui_sketch.connecting = true;
                connectedExpression = expressionBlock;
//                PApplet.println("I'm " + this.pinNum + " connecting to " + expressionNode);
            }
        }
    }

    public void mouseRelease() {
        if (gui_sketch.connecting && locked) {
            if (connectedPin == null && connectedExpression != null) {
//                gui_sketch.dataStructure.addConnection(this.node, connectedExpression.node);
                gui_sketch.background.connections.add(new Connection(gui_sketch, this, connectedExpression));
            } else if (connectedPin != null && !connectedPin.equals(this)) { // avoiding connection to itself
//                gui_sketch.dataStructure.addConnection(connectedPin.node, this.node);
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
            gui_sketch.text(selected, position.x - (size / 2) + 15, position.y);
        }
        gui_sketch.text(pinNum, position.x + 50, position.y + 50);
        gui_sketch.pop();
    }

}
