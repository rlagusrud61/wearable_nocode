package gui;

import main.IONode;
import main.Node;
import processing.core.PVector;

class OutputPin extends Pin {

    OutputPin(GUI_sketch gui_sketch, IONode node, PVector position) {
        super(gui_sketch, node, position);

        this.connectionPos = new PVector(position.x - 80, position.y + (size / 4));
        setShapes();
    }
}
