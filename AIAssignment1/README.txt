A wordsnake is a concatenation of a list of words with overlapping parts
example:
ending, lasting, blend, ingrate, terrible, 
can make the wordsnake lastingraterriblending

This assignment was to make a heuristic that maximized the overlap (details are in assignment.pdf) 


From the writeup:

Our original idea was to make use of a Genetic Algorithm (see https://secure.wikimedia.org/wikipedia/en/
wiki/Genetic_algorithm). What this means is that we devised a way to encode each "solution" uniformly, created
a "generation" of randomly generated solutions ("individuals"), then tested the "fitness" of each individual. The
most fit individuals have a better chance to mate (by combining their representation of a solution with another high-
scoring individual of the same generation). The newly created individuals from mating form the second generation.
The idea is that, given large enough population diversity, and enough generations, that the individuals will tend
more and more toward better solutions. This approach is much less time-consuming than a brute-force approach,
but more time-consuming than a simple heuristic. We believed that this could achieve better end results than a
heuristic might, so we decided to attempt this solution despite the greater runtime requirements.
In our specific implementation, we decided to encode an "individual" as a starting node, followed by n - 1 edges to
make a path through the original graph.

In practise, our genetic algorithm often got stuck on local maxima and stopped progressing after a certain point.
Although there is some degree of random chance in this algorithm, we could not seem to get answers as high as
heuristic approaches no matter how many times we ran our algorithm. The traditional solution to the problem
of local maxima in a genetic algorithm is to introduce "genetic mutation". This is the process of randomizing a
certain solution with very low probability to ensure greater diversity within a generation, and thereby have a greater
chance of getting out of a local maximum. Although this did solve our problem of getting stuck in local maxima, it
seemed to severely impede overall progress, so we were still unable to get good solutions with a reasonable number
of individuals and generations.

We believe that this approach could have worked if we had continued to experiment with different probabilities for
mating and mutation, however overall it seems to be less eficient at runtime than a simpler heuristic anyway.