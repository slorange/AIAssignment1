import java.util.ArrayList;

public class Node
{
   private String word;
   private boolean fromUsed = false;	//whether this node has been used as a from node
   private boolean toUsed = false;		//whether this node has been used as a from node
   private Node startNode = this;		//the starting node on this node's path
   private int index;						//the Nodes number in the Nodes array (Genetic uses this)

   private ArrayList<Edge> next = new ArrayList<Edge>();	//used by genetic

   public Node(String str, int i)
   {
      word = str;
      index = i;
   }

   public String getWord()
   {
      return word;
   }

   public void useFrom(){
      fromUsed = true;
   }

   public void useTo(){
      toUsed = true;
   }

   public boolean isFromUsed(){
      return fromUsed;
   }

   public boolean isToUsed(){
      return toUsed;
   }

   public Node getStartNode(){
      return startNode;
   }

   public void setStartNode(Node n){
      startNode = n;
   }

   public void addToNext(Edge e){
      next.add(e);
   }

   public int value(Node n){
      if(this == n)
      {
         // don't match words with themselves
         return -1;
      }
      int i = 0;
      String w1 = this.word;
      String w2 = n.word;
      int len1 = w1.length();
      int len2 = w2.length();
      if(len2 < len1)
      {
         i = len1 - len2;
      }
      while(i < len1 && !w1.substring(i).equals(w2.substring(0, len1 - i))){
         i++;
      }
      return len1 - i;
   }

   public Edge getNext(int i)
   {
      return next.get(i);
   }

   public int getNumNext()
   {
      return next.size();
   }

   public int getIndex(){
      return index;
   }
}
