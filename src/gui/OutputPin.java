package gui;

import processing.core.PVector;

class OutputPin extends Pin {

    //    private final GUI_sketch gui_sketch;
    OutputPin(GUI_sketch gui_sketch, String pinNum, PVector position) {
        super(gui_sketch, pinNum, position);
//        this.gui_sketch = gui_sketch;

        this.connectionPos = new PVector(position.x - 80, position.y + (size / 4));
        setShapes();
    }

//    public void display() {
//        super.display();
//        if (selected != null ) {
//            switch (selected) {
//                case "buzzer" :
//                    gui_sketch.push();
//                    gui_sketch.fill(0);
//                    gui_sketch.text("tone value " , position.x - size/2, position.y + 30);
//                    gui_sketch.pop();
//
//            }
//
//        }
//    }
}
