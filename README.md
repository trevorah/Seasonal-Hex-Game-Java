#Seasonal-Hex-Game-Java
##What is Hex?
Hex is a 2 player game played on a board of hexagonal tiles. It can be seen as having a similar complexity as chess or go, but with simple rules and a clear winning state that allows new players to quickly pick up and play with ease. The game offers intricacies that make the game rewarding to master and, combined with the impossibility of a draw-game, gives a satisfying game.

##What is the point of this implementation?
We will define and implement a generalisation of Hex called “Seasonal Hex”. This variant rules that players can only play on sets of tiles at a given time specified by a “Season”. We shall also implement Random Turn Hex, another generalisation of Hex that decides which player can move by a coin toss rather than a turn by turn basis.

For both generalisations, we will design a variety of Artificial intelligence (AI) players that can not only play to these rules, but also take advantage of the nuances that they offer. To observe the implementations of these new rules and players, we will create a stable platform on which to experiment with the details of each design and observe their results.

##How to build
	mvn clean install