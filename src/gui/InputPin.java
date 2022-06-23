package gui;

import main.IONode;
import main.Node;
import processing.core.PVector;

class InputPin extends Pin {

    InputPin(GUI_sketch gui_sketch,String pinNum,  PVector position) {
        super(gui_sketch, pinNum,position);
        this.connectionPos = new PVector(position.x + size * 0.55f, position.y);

        setShapes();
    }


}
