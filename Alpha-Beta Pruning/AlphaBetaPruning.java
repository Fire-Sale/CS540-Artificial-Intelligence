import java.util.*;

public class AlphaBetaPruning {
	private int move;
	private double value;
	private int numNodeVisited = 0;
	private int numNodeEvaluated = 0;
	private int maxDepth = -1;
	private double branchFactor = 0;

	private int requiredDepth;
	private ArrayList<Double> valueList = new ArrayList<>();
	private ArrayList<Integer> numBranchNode = new ArrayList<>();

	public AlphaBetaPruning() {
	}

	/**
	 * This function will print out the information to the terminal, as specified in
	 * the homework description.
	 */
	public void printStats() {
		System.out.println("Move: " + move);
		System.out.println("Value: " + value);
		System.out.println("Number of Nodes Visited: " + numNodeVisited);
		System.out.println("Number of Nodes Evaluated: " + numNodeEvaluated);
		System.out.println("Max Depth Reached: " + maxDepth);
		System.out.println("Avg Effective Branching Factor: " + branchFactor);
	}

	/**
	 * This function will start the alpha-beta search
	 * 
	 * @param state This is the current game state
	 * @param depth This is the specified search depth
	 */
	public void run(GameState state, int depth) {
		requiredDepth = depth;
		int numStoneTaken = 0;
		for (int i = 1; i < state.getSize(); i++) {
			if (state.getStone(i) == false)
				numStoneTaken++;
		}
		boolean turn = (numStoneTaken % 2 == 0 ? true : false);
		value = alphabeta(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, turn);
		int idx = valueList.indexOf(value);
		List<Integer> nextMoves = state.getMoves();
		move = nextMoves.get(idx);
		for (Integer i : numBranchNode)
			branchFactor += i;
		branchFactor /= numBranchNode.size();
	}

	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * 
	 * @param state     This is the current game state
	 * @param depth     Current depth of search
	 * @param alpha     Current Alpha value
	 * @param beta      Current Beta value
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating score of the best next move
	 */
	private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
		List<GameState> successorList = state.getSuccessors();
		double value;
		int numNode = 0;
		maxDepth = Math.max(maxDepth, requiredDepth - depth);
		numNodeVisited++;
		if (depth == 0 || successorList.size() == 0) {
			value = state.evaluate();
			if (depth == requiredDepth - 1)
				valueList.add(value);
			numNodeEvaluated++;
			return value;
		}
		if (maxPlayer) {
			value = Double.NEGATIVE_INFINITY;
			for (GameState successor : successorList) {
				numNode++;
				value = Math.max(value, alphabeta(successor, depth - 1, alpha, beta, false));
				if (value >= beta)
					break;
				alpha = Math.max(alpha, value);
			}
		} else {
			value = Double.POSITIVE_INFINITY;
			for (GameState successor : successorList) {
				numNode++;
				value = Math.min(value, alphabeta(successor, depth - 1, alpha, beta, true));
				if (value <= alpha)
					break;
				beta = Math.min(value, beta);
			}
		}
		numBranchNode.add(numNode);
		if (depth == requiredDepth - 1)
			valueList.add(value);
		return value;
	}
}
