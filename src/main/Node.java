package main;

import java.sql.Array;
import java.util.ArrayList;

public abstract class Node {

   public String port;
   ArrayList<Node> children;
   Node(String port){
      children = new ArrayList<>();
      this.port = port;

   }

   public void addChild(Node child){
      children.add(child);
   }

   public Node getChild(){
     //return
      return null;
   }


}
