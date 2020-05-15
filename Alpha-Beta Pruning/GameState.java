import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

public class GameState {
    private int size; // The number of stones
    private boolean[] stones; // Game state: true for available stones, false for taken ones
    private int lastMove; // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        // For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }

    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
        List<Integer> nextMoves = new ArrayList<>();
        if (lastMove == -1) {
            for (int i = 1; i < size / 2.0; i++) {
                if (i % 2 == 1) {
                    nextMoves.add(i);
                }
            }
            return nextMoves;
        } 
        for (int i = 1; i <= size; i++) {
            if (stones[i] == true){
                if (i % lastMove == 0 || lastMove % i == 0) {
                    nextMoves.add(i);
                }
            }
        }
        return nextMoves;
    }

    /**
     * This method is used to generate a list of successors using the getMoves()
     * method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }

    /**
     * This method is used to evaluate a game state based on the given heuristic
     * function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        List<Integer> nextMoves = new ArrayList<Integer>();
        nextMoves = getMoves();
        int numNextMoves = nextMoves.size();
        int numStoneTaken = 0;
        int cnt = 0;
        double value;
        if (numNextMoves == 0) {
            value = -1.0;
        } else if (stones[1] == true) {
            value = 0;
        } else if (lastMove == 1) {
            value = (numNextMoves % 2 == 0 ? 0.5 : -0.5);
        } else if (Helper.isPrime(lastMove)) {
            for (Integer i:nextMoves) {
                if (i % lastMove == 0)
                    cnt++;
            }
            value = (cnt % 2 == 0 ? 0.7 : -0.7);
        } else {
            int largestPrime = Helper.getLargestPrimeFactor(lastMove);
            for (Integer i:nextMoves) {
                if (i % largestPrime == 0) {
                    cnt++;
                }
            }
            value = (cnt % 2 == 0 ? 0.6 : -0.6);
        }
        for (int i = 1; i < size; i++) {
            if (stones[i] == false)
                numStoneTaken++;
        }
        if (numStoneTaken % 2 == 0)
            return value;
        else
            return -value;
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }
}
