public class Edge implements Comparable<Edge>
{
   private int value;
   private Node fromNode;
   private Node toNode;
   private int opportunityCost = -1;
   private int totalCost = -1;

   public Edge(Node from, Node to, int val)
   {
      fromNode = from;
      toNode = to;
      value = val;
   }

   public Edge(Node from, Node to)
   {
      fromNode = from;
      toNode = to;
      value = from.value(to);
   }


   public Node getFromNode()
   {
      return fromNode;
   }

   public Node getToNode()
   {
      return toNode;
   }

   public int getValue()
   {
      return value;
   }

   public void setTo(Node to)
   {
      toNode = to;
   }

   public void setFrom(Node from)
   {
      fromNode = from;
   }

   public void setValue(int val)
   {
      value = val;
   }

   //returns the next best alternative for the from and to node
   public int getOpportunityCost(){
      if(opportunityCost == -1){
         Edge[] edges = Graph.edges;
         int fromCost = 0, toCost = 0;
         for(int i = 0; i < edges.length; i++){
            Edge e = edges[i];
            if(this != e && fromNode == e.fromNode){
               if(fromCost < e.value){
                  fromCost = e.value;
               }
            }
            if(this != e && toNode == e.toNode){
               if(toCost < e.value){
                  toCost = e.value;
               }
            }
         }
         opportunityCost = Math.max(fromCost,toCost);
      }
      return opportunityCost;
   }

   //returns the total value of each node the to and from node could have been used for
   public int getTotalCost(){
      if(totalCost == -1){
         Edge[] edges = Graph.edges;
         int cost = 0;
         for(int i = 0; i < edges.length; i++){
            Edge e = edges[i];
            if(this != e && fromNode == e.fromNode){
               cost += e.value;
            }
            if(this != e && toNode == e.toNode){
               cost += e.value;
            }
         }
         totalCost = cost;
      }
      return totalCost;
   }

   //used by sort to make a path
   //implementation varies with algorithm
   public int compareTo(Edge e) {

      if(Wordsnake.solution == 1){
         return this.value - this.getTotalCost() - (e.value - e.getTotalCost());
      }
      else if(Wordsnake.solution == 2){
         return this.value - this.getOpportunityCost() - (e.value - e.getOpportunityCost());
      }
      else{
         return this.value - e.value;
      }
   }

   public String toString(){
      if(Wordsnake.solution == 1 && value > 0){
         return fromNode.getWord() + " --> " + toNode.getWord() + " --- " + value + " " + totalCost;
      }
      else if(Wordsnake.solution == 2 && value > 0){
         return fromNode.getWord() + " --> " + toNode.getWord() + " --- " + value + " " + opportunityCost;
      }
      else{
         return fromNode.getWord() + " --> " + toNode.getWord() + " --- " + value;
      }
   }
}
