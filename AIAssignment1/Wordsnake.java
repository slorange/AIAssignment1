import java.io.BufferedWriter;
import java.util.Random;

public class Wordsnake
{
   public static int solution = 2;
   public static String testName = "test1";
   //0 -> greedy
   //1 -> total cost
   //2 -> opportunity cost
   //3 -> genetic algorithm

   public static Random r = null;
   public static int NUMBER_OF_GENERATIONS = 3000;
   public static BufferedWriter out;

   public static void main(String[] args)
   {
      String inputFile = testName+".txt";
      if(args.length >= 1){
         inputFile = args[0];
      }

	  //for genetic algorithm
      if(solution == 3){
         long seed = System.currentTimeMillis();
         if(args.length >= 2){
            seed = Integer.parseInt(args[1]);
         }
         System.out.println("Using seed " + seed);
         r = new Random(seed);
      }

      Graph newGraph = new Graph(args, inputFile);
      int bufReader = newGraph.makeGraph();
      if(bufReader == -1)
      {
         System.out.println("Could not open the file");
         return;
      }

	  //for heuristic algorithms
      if(solution < 3){

         Edge[] path = newGraph.getPath();
         path = Graph.organizePath(path);

         int score = 0;
         for(int i = 0; i < path.length; i++){
            score += Math.pow(path[i].getValue(), 2);
            System.out.println("" + path[i]);
         }
         System.out.println(""+Graph.pathString(path));
         System.out.println("Maximal number = " + score);
      }
      else{//genetic algorithm

         //parameters that we can play with:
         //Genetic::mutationRate
         //Generation::k (number of Genetics/Generation)
         //Generation's killing off Genetics algorithm
         //WordSnakes NUMBER_OF_GENERATIONS

         Generation g = new Generation();
         for(int i = 0; i < NUMBER_OF_GENERATIONS; i++){
            g = g.nextGeneration();
			//only output every 10 generations
            if(i%10 == 9)
               System.out.println("At generation " + (i+1) + " best score is " + g.getBestGenetic().getScore()
                     + " and the average score is " + g.getAverageScore());
         }
         System.out.println("Maximal number = " + g.getBestGenetic().getScore());
         System.out.println(g.getBestGenetic().toString());
      }
   }
}
