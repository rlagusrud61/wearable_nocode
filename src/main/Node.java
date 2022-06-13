package main;

import java.sql.Array;
import java.util.ArrayList;

public abstract class Node {

   ArrayList<Node> children;
   Node(){
      children = new ArrayList<>();
   }

   public void addChild(Node child){
      children.add(child);
   }

   public Node getChild(){
     //return
      return null;
   }


}
