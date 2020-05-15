import java.util.*;

/**
 * Class for internal organization of a Neural Network. There are 5 types of
 * nodes. Check the type attribute of the node for details. Feel free to modify
 * the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; // 0 = input, 1 = biasToHidden, 2 = hidden, 3 = biasToOutput, 4 = Output
    public ArrayList<NodeWeightPair> parents = null; // Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; // input gradient

    // Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);
        } else
            this.type = type;
        if (type == 2 || type == 4)
            parents = new ArrayList<>();
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type != 1 && type != 3)
            this.inputValue = inputValue;
    }

    public double getOutput() {
        if (type == 0)
            return inputValue;
        else if (type == 1 || type == 3)
            return 1;
        else
            return outputValue;
    }

    public void getInput() {
        if (type == 2 || type == 4) {
            double sum = 0;
            for (NodeWeightPair pair : parents)
                sum += pair.node.getOutput() * pair.weight;
            this.inputValue = sum;
        }
    }

    public void calculateOutput(ArrayList<Node> outputNodes) {
        if (type == 2)
            this.outputValue = Math.max(this.inputValue, 0);
        else if (type == 4) {
            double sum = 0;
            for (Node node : outputNodes)
                sum += Math.exp(node.inputValue);
            this.outputValue = Math.exp(this.inputValue) / sum;
        }
    }

    // Calculate the delta value of a node
    public void calculateDelta(ArrayList<Node> outputNodes, int idx, double targetValue) {
        // TODO: add code here
        if (type == 4)
            this.delta = targetValue - this.outputValue;
        else if (type == 2) {
            double sum = 0;
            for (Node node : outputNodes)
                sum += node.parents.get(idx).weight * node.delta;
            if (this.inputValue > 0)
                this.delta = sum;
            else
                this.delta = 0;
        }
    }

    // Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
            // TODO: add code here
            for (NodeWeightPair pair : this.parents)
                pair.weight += learningRate * pair.node.getOutput() * delta;
        }
    }
}
