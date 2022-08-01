package gui;

import processing.core.PVector;

class OutputPin extends Pin {

    //    private final GUI_sketch gui_sketch;
    OutputPin(GUI_sketch gui_sketch, String pinNum, PVector position) {
        super(gui_sketch, pinNum, position);
        this.connectionPos = new PVector(position.x - SIZE * 0.55f, position.y);
        setShapes();
    }

    public void display() {
        super.display();
        if (selected != null) {
            switch (selected) {
                case "BUZZER":
                    gui_sketch.push();
                    gui_sketch.fill(0);
                    gui_sketch.text("tone value ", position.x - SIZE / 2, position.y + 30);
                    connectionBox1.setVisible(false);
                    connectionBox3.setVisible(false);
                    gui_sketch.pop();
                    break;
                case "NEOPIXEL":
                    gui_sketch.push();
                    gui_sketch.fill(0);
                    gui_sketch.text("color", position.x - SIZE / 2, position.y );
                    gui_sketch.text("intensity", position.x - SIZE / 2, position.y + 30);
                    gui_sketch.text("pattern", position.x - SIZE / 2, position.y + 60);
                    gui_sketch.pop();
                    break;
            }

        }
    }
}
