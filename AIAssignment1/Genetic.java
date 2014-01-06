import java.util.Random;

public class Genetic
{
   private Node startGene;		//one gene for the start node
   private Edge[] edgeGenes;	//one gene for each edge in the path
   private int mateLower;		//used by generation to determine which genetics get to mate
   private int mateUpper;		//used by generation to determine which genetics get to mate
   private static Random rand = Wordsnake.r;
   private static Node[] allNodes = null;
   private int score = -1;		//score of the path

   public static final double MUTATION_RATE = 0.00; // TODO experiment with this value

   //creating the first generation of genetics
   public Genetic(){
      int num = allNodes.length;
      edgeGenes = new Edge[num-1];
      startGene = allNodes[rand.nextInt(allNodes.length)];
      boolean[] available = new boolean[num];
      for(int i = 0; i < num; i++){
         available[i] = true;
      }
      Node prevNode = startGene;
      available[startGene.getIndex()] = false;

      //for each nodes, pick a node to go to
      for(int x = 0; x < num-1; x++){
         boolean random = true;
         int numNext = prevNode.getNumNext();
         if(numNext > 0){
            Node nextNode = prevNode.getNext(rand.nextInt(numNext)).getToNode();
            if(available[nextNode.getIndex()]){
               edgeGenes[x] = new Edge(prevNode, nextNode);
               available[nextNode.getIndex()] = false;
               random = false;
               prevNode = nextNode;
            }
         }
         if(random){//the nodes has no available nodes to go to so we pick one randomly and add a zero edge
            int i = -1;
            int c = 0;
            for(i = 0; i < num; i++) //count the number of available nodes left
               if(available[i])
                  c++;
            i = -1;
            int j = rand.nextInt(c);
            while(j > -1){ //get the j'th number from the available array
               i++;
               if(available[i])
                  j--;
            }
            edgeGenes[x] = new Edge(prevNode, allNodes[i]);
            available[i] = false;
            prevNode = allNodes[i];
         }
      }
   }

   //used in mate
   public Genetic(Node start, Edge[] edges)
   {
      startGene = start;
      edgeGenes = edges;
      mateLower = -1;
      mateUpper = -1;
   }

   //called by graph
   public static void setNodes(Node[] nodes)
   {
      allNodes = nodes;
   }

   //used in generation to get the best genetic and decide on which genetics get to mate
   public int getScore(){
      if(score == -1){
         score = 0;
         int numEdges = edgeGenes.length;
         for(int x = 0; x < numEdges; x++){
            score += Math.pow(edgeGenes[x].getValue(),2);
         }
      }
      return score;
   }

   public void setMateChanceLower(int i)
   {
      mateLower = i;
   }

   public void setMateChanceUpper(int i)
   {
      mateUpper = i;
   }

   public int getMateChanceLower()
   {
      return mateLower;
   }

   public int getMateChanceUpper()
   {
      return mateUpper;
   }

   //O(n)
   public Edge getNext(Node n){
      for(int i = 0; i < edgeGenes.length; i++){
         if(edgeGenes[i].getFromNode() == n){
            return edgeGenes[i];
         }
      }
      return null;
   }

   //These are used only by mate and its helper function but since we can't pass some of these by reference we need to declare outside
   private Node prevNode;
   private boolean[] available = new boolean[allNodes.length];
   private Node childStart;
   private Edge[] childEdges = new Edge[allNodes.length-1];
   private int childNumber;

   //O(n^2)
   //small chance to mutate at each step
   //starts by picking one of the parents startnodes
   //for each edge it picks randomly between one of the parents to nodes
   //if it mutates or if the parent's is unavailable, itll pick an available edge randomly
   public Genetic mate(Genetic g2)
   {
      Genetic g1 = this;
      int len = g1.getSize();
      if(len != g2.getSize()){
         System.out.println("attempted to mate Genetics with different sizes");
         return null; // these Genetics aren't compatible and shouldn't be mated
      }
      if(len != allNodes.length)
         System.out.println("found a Genetic with an incorrect number of edges");

      childNumber = 0;
      for(int i = 0; i < len; i++){
         available[i] = true;
      }

      //start gene
      double r = rand.nextDouble();
      if(r > MUTATION_RATE / 5){ //use one of the parents
         if(rand.nextBoolean())
            childStart = g1.startGene;
         else
            childStart = g2.startGene;
      }
      else //use a random one
         childStart = allNodes[rand.nextInt(len)];
      available[childStart.getIndex()] = false;

      prevNode = childStart;
      //edge genes
      for(int i = 0; i < len-1; i++){
         double mutationRate = MUTATION_RATE;
         if(g1.getNext(prevNode) != null)
            mutationRate /= g1.getNext(prevNode).getValue();
         if(g2.getNext(prevNode) != null)
            mutationRate /= g2.getNext(prevNode).getValue();
         r = rand.nextDouble();
         boolean mutate = r < mutationRate;
         if(!mutate){ //use one of the parents
            if(rand.nextBoolean()){
               if(!useParentsNextEdge(g1)){//try the first parent
                  if(!useParentsNextEdge(g2)){//try the second parent if the first fails
                     mutate = true;//both parents failed so we have to mutate
                  }
               }
            }
            else{
               if(!useParentsNextEdge(g2)){//try the second parent
                  if(!useParentsNextEdge(g1)){//try the first parent if the second fails
                     mutate = true;//both parents failed so we have to mutate
                  }
               }
            }
         }
         if(mutate){
            //make new edge from lastNode to a random one in available
            int c = 0;
            for(int k = 0; k < len; k++) //count the number of available nodes left
               if(available[k])
                  c++;

            int j = rand.nextInt(c);
            int k = -1;
            while(j > -1){ //get the j'th number from the available array
               k++;
               if(available[k])
                  j--;
            }
            Node n = allNodes[k];
            childEdges[childNumber] = new Edge(prevNode, n);
            available[k] = false;
            prevNode = n;
            childNumber++;
         }
      }

      return new Genetic(childStart, childEdges);
   }

   //helper function for mate()
   //O(n)
   private boolean useParentsNextEdge(Genetic parent){
      Edge nextEdge = parent.getNext(prevNode);//O(n)
      if(nextEdge == null)
         return false;
      Node nextNode = nextEdge.getToNode();
      boolean avail = available[nextNode.getIndex()];
      if(avail){
         childEdges[childNumber] = nextEdge;
         childNumber++;
         available[nextNode.getIndex()] = false;
         prevNode = nextNode;
      }
      return avail;
   }

   public int getSize(){
      return edgeGenes.length+1;
   }

   public String toString(){
      StringBuilder s = new StringBuilder();
      for(int i = 0; i < edgeGenes.length; i++){
         s.append(edgeGenes[i]);
         s.append("\n");
      }
      return s.toString();
   }
}
