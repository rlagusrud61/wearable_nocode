package gui;

import processing.core.PConstants;

class Menu {

    private final GUI_sketch gui_sketch;
    boolean isActive;

    Pin pin;
    Choices choices;

    Menu(GUI_sketch gui_sketch, Pin pin) {
        this.gui_sketch = gui_sketch;
        this.pin = pin;
        choices = new Choices(pin);
    }

    public void show() {
        isActive = true;
        display();
        choices.show();
    }

    public void hide() {
        isActive = false;
        choices.hide();
    }


    public void display() {
        gui_sketch.background(225);
        gui_sketch.push();
        gui_sketch.rectMode(PConstants.CENTER);
        gui_sketch.translate(gui_sketch.width / 2, gui_sketch.height / 2);
        gui_sketch.rect(0, 0, 600, 400, 10);
        gui_sketch.pop();
        gui_sketch.push();
        gui_sketch.fill(0);
        gui_sketch.text("Menu for pin " + pin.pinNum, 200, 250);
        gui_sketch.pop();


    }
}
