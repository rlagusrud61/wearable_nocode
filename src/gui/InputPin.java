package gui;

import processing.core.PVector;

class InputPin extends Pin {

    InputPin(GUI_sketch gui_sketch,String pinNum,  PVector position) {
        super(gui_sketch, pinNum,position);
        this.connectionPos = new PVector(position.x + SIZE * 0.55f, position.y);

        setShapes();
    }


    public void display() {
        super.display();
        if (selected != null) {
            switch (selected) {
                case "TOUCH":
                    gui_sketch.push();
                    gui_sketch.fill(0);
                    gui_sketch.text("toggle", position.x - SIZE / 2, position.y + 30);
                    connectionBox1.setVisible(false);
                    connectionBox3.setVisible(false);
                    gui_sketch.pop();
                    break;

//                    More cases need to be implemented here.
            }

        }
    }


}
