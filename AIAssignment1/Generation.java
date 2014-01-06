public class Generation {
   private Genetic[] genetics;
   public static final int k = 350;
   private int totalScore;
   Genetic bestGeneticEver;

   public Generation(){
      totalScore = 0;
      genetics = new Genetic[k];
      for(int x = 0; x < k; x++)
      {
         genetics[x] = new Genetic();
      }
   }

   private Generation(Genetic[] gen, Genetic best){
      totalScore = 0;
      // k = gen.size();
      if(k != gen.length)
      {
         //TODO fail horribly
      }
      genetics = gen;

      bestGeneticEver = best;
   }

   public Generation nextGeneration(){
      //get total score and set changes to mate
      for(int i = 0; i < k; i++){
         Genetic g = genetics[i];
         g.setMateChanceLower(totalScore+1);
         totalScore += (Math.pow(g.getScore(), 2));
         g.setMateChanceUpper(totalScore);
      }
      //get k children
      Genetic[] children = new Genetic[k];
      for(int i = 0; i < k; i++){
         children[i] = newChild();
      }
      return new Generation(children, getBestGenetic());
   }

   private Genetic newChild(){
      int r1 = 0;

      if(totalScore != 0)
      {
         r1 = Wordsnake.r.nextInt(totalScore) + 1;
      }

      Genetic p1 = null;
      for(int i = 0; i < k; i++){
         Genetic g = genetics[i];
         if(r1 >= g.getMateChanceLower() && r1 <= g.getMateChanceUpper()){
            p1 = g;
         }
      }
      if(p1 == null)
         System.out.println("error1 in Generation.java " + totalScore + " " + r1);
      Genetic p2 = null;
      while(p2 == null){
         int r2 = 0;
         if(totalScore != 0)
         {
            r2 = Wordsnake.r.nextInt(totalScore);
         }
         for(int i = 0; i < k; i++){
            Genetic g = genetics[i];
            if(r2 >= g.getMateChanceLower() && r2 <= g.getMateChanceUpper()){
               if(g != p1){
                  p2 = g;
                  break;
               }
            }
         }
      }

      return p1.mate(p2);
   }

   public int getAverageScore(){
      int total = 0;
      for(int i = 0; i < k; i++){
         Genetic g = genetics[i];
         total += g.getScore();
      }
      return total / k;
   }

   public Genetic getBestGenetic(){
      int highestScore;
      if(bestGeneticEver == null)
         highestScore = 0;
      else
         highestScore = bestGeneticEver.getScore();
      for(int i = 0; i < k; i++){
         Genetic g = genetics[i];
         if(highestScore < g.getScore()){
            highestScore = g.getScore();
            bestGeneticEver = g;
         }
      }
      return bestGeneticEver;
   }
}
