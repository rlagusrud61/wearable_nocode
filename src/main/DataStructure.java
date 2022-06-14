package main;


import java.util.ArrayList;

public class DataStructure {

    public RootNode root;
    public ArrayList<Node> nodes;

    public DataStructure() {
        init();
    }

    public void init() {

        this.root = new RootNode();

        //initiate all the nodes in the program
        nodes = new ArrayList<>();

        SensorNode A0 = new SensorNode("A0");
        SensorNode A1 = new SensorNode("A1");
        SensorNode A2 = new SensorNode("A2");
        ActuatorNode D9 = new ActuatorNode("D9") ;
        ActuatorNode D10 = new ActuatorNode("D10") ;
        ActuatorNode D11 = new ActuatorNode("D11") ;

        ExpressionNode exp = new ExpressionNode();

        nodes.add(A0);
        nodes.add(A1);
        nodes.add(A2);
        nodes.add(D9);
        nodes.add(D10);
        nodes.add(D11);
        nodes.add(exp);


    }

    public void addConnection(Node parent, Node child){
        parent.addChild(child);
    }

}
