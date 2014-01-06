import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Graph
{
   private String inputFileName = null;
   public static Node[] nodes;
   public static Edge[] edges;
   ArrayList<Edge> path = new ArrayList<Edge>();

   public Graph(String[] args, String defaultTest)
   {
      if(args.length >= 1){
         inputFileName = args[0];
      }
      else{
         inputFileName = defaultTest;
      }
   }

   public int makeGraph()
   {
      Scanner input = null;

      try{
         input = new Scanner(new File(inputFileName));
      }
      catch(Exception e){
         try{
            input = new Scanner(new File("src/"+inputFileName));
         }
         catch(Exception e2){
            return -1;
         }
      }

      ArrayList<Node> nodesTemp = new ArrayList<Node>();
      ArrayList<Edge> edgesTemp = new ArrayList<Edge>();

      //create all nodes
      int i = 0;
      while(input.hasNextLine())
      {
         Node n = new Node(input.nextLine(), i);
         nodesTemp.add(n);
         i++;
      }

      //create all edges
      for(i = 0; i < nodesTemp.size(); i++){
         Node n1 = nodesTemp.get(i);
         for(int j = 0; j < nodesTemp.size(); j++){
            Node n2 = nodesTemp.get(j);
            int value = n1.value(n2);
            if(value > 0){
               Edge e = new Edge(n1, n2, value);
               n1.addToNext(e);
               edgesTemp.add(e);
            }
         }
      }
      nodes = new Node[nodesTemp.size()];
      edges = new Edge[edgesTemp.size()];
      nodesTemp.toArray(nodes);
      edgesTemp.toArray(edges);

      Genetic.setNodes(nodes);

      return 0;
   }

   //sort the edges using their comparison function
   //use the edges in decreasing order
   public Edge[] getPath(){
      Arrays.sort(edges);
      for(int i = edges.length-1; i >= 0; i--){
         Edge e = edges[i];
         Node from = e.getFromNode();
         Node to = e.getToNode();
         if(!from.isFromUsed() && !to.isToUsed() && from.getStartNode() != to)
            addEdgeToPath(e);
      }

      Edge[] rtn = new Edge[path.size()];
      path.toArray(rtn);
      return rtn;
   }
   
   //helper for getPath()
   private void addEdgeToPath(Edge e){
      Node from = e.getFromNode();
      Node to = e.getToNode();
      from.useFrom();
      to.useTo();
      //for all nodes that have startNode set as the to node, change it to the from node
      for(int j = 0; j < nodes.length; j++){
         if(nodes[j].getStartNode() == to){
            nodes[j].setStartNode(from.getStartNode());
         }
      }
      path.add(e);
   }

   //used after getPath to order it nicely and to show 0 edges
   public static Edge[] organizePath(Edge[] path){
      Edge[] organized = new Edge[nodes.length-1];
      int count = 0;
      Node to = null;

      for(int i = 0; i < path.length; i++){
         Edge e = path[i];
         Node from = e.getFromNode();
         if(!from.isToUsed()){//if the from node hasnt been used as a to node, then its a start node
            if(to != null){//create 0 node between paths
               organized[count] = new Edge(to,from);
               count++;
            }
            to = e.getToNode();
            organized[count] = e;
            count++;
            while(to.isFromUsed()){//while the to node hasnt been used as a from node, we havent reached the end of the path
               for(int j = 0; j < path.length; j++){//find next one
                  Edge e2 = path[j];
                  if(e2.getFromNode() == to){
                     from = to;
                     to = e2.getToNode();
                     organized[count] = e2;
                     count++;
                     break;
                  }
               }
            }
         }
      }
      for(int i = 0; i < nodes.length; i++){
         if(!nodes[i].isToUsed() && !nodes[i].isFromUsed()){
            if(to == null){
               nodes[nodes.length-1].useTo();
               to = nodes[nodes.length-1];
            }
            organized[count] = new Edge(to,nodes[i]);
            count++;
            to = nodes[i];
         }
      }
      //System.out.println(count + " " + (nodes.length-1));
      return organized;
   }

   //print out the wordsnake
   public static String pathString(Edge[] edges){
      String s = "";
      if(edges.length > 0)
         s = edges[0].getFromNode().getWord();
      for(int i = 0; i < edges.length; i++){
         s += edges[i].getToNode().getWord().substring(edges[i].getValue());
      }
      return s;
   }

   public void printNodes()
   {
      for(int i = 0; i < nodes.length; i++){
         System.out.println(nodes[i].getWord());
      }
   }

   public int size()
   {
      return nodes.length;
   }
}
