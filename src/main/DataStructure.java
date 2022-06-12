package main;


import processing.core.PApplet;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class DataStructure {

    public ArrayList<Node> nodes;

    public DataStructure() {
        init();
    }

    public void init() {

        //initiate all the nodes in the program
        nodes = new ArrayList<>();

        SensorNode A0 = new SensorNode("A0");
        SensorNode A1 = new SensorNode("A1");
        SensorNode A2 = new SensorNode("A2");
        ActuatorNode D9 = new ActuatorNode("D9") ;
        ActuatorNode D10 = new ActuatorNode("D10") ;
        ActuatorNode D11 = new ActuatorNode("D11") ;

        nodes.add(A0);
        nodes.add(A1);
        nodes.add(A2);
        nodes.add(D9);
        nodes.add(D10);
        nodes.add(D11);

    }

    public void addConnection(Node n1, Node n2){
        if(n1 instanceof ActuatorNode){
            PApplet.println("here");
            n1.addChild(n2);
        } else if (n2 instanceof ActuatorNode){
            n2.addChild(n1);
        }

    }

}
