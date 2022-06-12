package gui;

import main.Node;
import processing.core.PVector;

class InputPin extends Pin {

    InputPin(GUI_sketch gui_sketch, Node node,  PVector position) {
        super(gui_sketch,node, position);
        this.connectionPos = new PVector(position.x + size * 0.55f, position.y);

        setShapes();
    }


}
