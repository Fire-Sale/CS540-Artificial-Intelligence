import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with
		// a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// Queue implementing the Frontier list

		LinkedList<State> queue = new LinkedList<State>();
		queue.push(new State(this.maze.getPlayerSquare(), null, 0, 0));
		maxSizeOfFrontier = 1;

		while (!queue.isEmpty()) {
			State currentState = queue.pop();
			explored[currentState.getX()][currentState.getY()] = true;

			maxDepthSearched = Math.max(currentState.getDepth(), maxDepthSearched);
			++noOfNodesExpanded;

			if (currentState.isGoal(maze)) {
				cost = currentState.getGValue();
				while (currentState.getParent() != null) {
					currentState = currentState.getParent();
					// maze.setOneSquare(currentState, '.');
					maze.setOneSquare(currentState.getSquare(), '.');
				}
				maze.setOneSquare(maze.getPlayerSquare(), 'S');
				return true;
			}

			ArrayList<State> successors = currentState.getSuccessors(explored, maze);
			for (State successor : successors) {
				if (!queue.contains(successor) && !explored[successor.getX()][successor.getY()]) {
					queue.add(successor);
				}
			}
			maxSizeOfFrontier = Math.max(maxSizeOfFrontier, queue.size());
		}

		// TODO return true if find a solution
		// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
		// maxDepthSearched, maxSizeOfFrontier during
		// the search
		// TODO update the maze if a solution found

		// use queue.pop() to pop the queue.
		// use queue.add(...) to add elements to queue

		return false;
		// TODO return false if no solution
	}

}
