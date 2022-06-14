package main;

public class IONode extends Node{

    public String port;
    public String label;

    IONode(String port){
       this.port = port;
    }

    public void setLabel(String label){
       this.label = label;
    }
}
