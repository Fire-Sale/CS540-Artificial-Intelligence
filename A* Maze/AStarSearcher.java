import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Iterator;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a
		// given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		State start = new State(this.maze.getPlayerSquare(), null, 0, 0);
		Square end = maze.getGoalSquare();
		frontier.add(new StateFValuePair(start, distance(start.getSquare(), end)));
		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs

			StateFValuePair current = frontier.poll();
			State currentState = current.getState();
			explored[currentState.getX()][currentState.getY()] = true;
			maxDepthSearched = Math.max(maxDepthSearched, currentState.getDepth());
			++noOfNodesExpanded;

			if (currentState.isGoal(maze)) {
				cost = currentState.getGValue();
				currentState = currentState.getParent();
				while (currentState.getParent() != null) {
					maze.setOneSquare(currentState.getSquare(), '.');
					currentState = currentState.getParent();
				}
				return true;
			}
			ArrayList<State> successors = currentState.getSuccessors(explored, maze);
			for (State successor : successors) {
				double fValue = successor.getGValue() + distance(successor.getSquare(), end);
				StateFValuePair q = new StateFValuePair(successor, fValue);
				if (!explored[successor.getX()][successor.getY()] && !frontier.contains(q)) {
					frontier.add(q);
					continue;
				}
				for (StateFValuePair p : frontier) {
					if (successor.getX() == p.getState().getX() && successor.getY() == p.getState().getY()) {
						if (p.getFValue() > fValue) {
							frontier.remove(p);
							frontier.add(q);
							break;
						}
					}
				}
			}
			maxSizeOfFrontier = Math.max(this.maxSizeOfFrontier, frontier.size());

		}
		return false;
	}

	private double distance(Square s1, Square s2) {
		return Math.sqrt(Math.pow(Math.abs(s1.X - s2.X), 2) + Math.pow(Math.abs(s1.Y - s2.Y), 2));
	}
}
