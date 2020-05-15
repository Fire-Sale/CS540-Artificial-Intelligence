import java.util.*;

/**
 * The main class that handles the entire network Has multiple attributes each
 * with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; // list of the output layer nodes.
    private ArrayList<Node> hiddenNodes; // list of the hidden layer nodes
    private ArrayList<Node> outputNodes; // list of the output layer nodes

    private ArrayList<Instance> trainingSet; // the training set

    private double learningRate; // variable to store the learning rate
    private int maxEpoch; // variable to store the maximum number of epochs
    private Random random; // random number generator to shuffle the training set

    /**
     * This constructor creates the nodes necessary for the neural network Also
     * connects the nodes of different layers After calling the constructor the last
     * node of both inputNodes and hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random,
           Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.maxEpoch = maxEpoch;
        this.random = random;

        // input layer nodes
        inputNodes = new ArrayList<>();
        int inputNodeCount = trainingSet.get(0).attributes.size();
        int outputNodeCount = trainingSet.get(0).classValues.size();
        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        // bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        // hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            // Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
            }
            hiddenNodes.add(node);
        }

        // bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        // Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            // Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
                node.parents.add(nwp);
            }
            outputNodes.add(node);
        }
    }

    /**
     * Get the prediction from the neural network for a single instance Return the
     * idx with highest output values. For example if the outputs of the outputNodes
     * are [0.1, 0.5, 0.2], it should return 1. The parameter is a single instance
     */

    public int predict(Instance instance) {
        // TODO: add code here
        double finalValue = 0;
        double maxFinalValue = Double.MIN_VALUE;
        int maxIndex = -1;
        fowardComp(instance);
        for (int i = 0; i < this.outputNodes.size(); i++) {
            finalValue = this.outputNodes.get(i).getOutput();
            if (finalValue > maxFinalValue) {
                maxFinalValue = finalValue;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Train the neural networks with the given parameters
     * The parameters are stored as attributes of this class
     */

    public void train() {
        // TODO: add code here
        for (int epoch = 0; epoch < this.maxEpoch; epoch++) {
            Collections.shuffle(this.trainingSet, this.random);
            for (Instance instance : this.trainingSet) {
                fowardComp(instance);
                for (int i = 0; i < this.outputNodes.size(); i++)
                    this.outputNodes.get(i).calculateDelta(this.outputNodes, i, instance.classValues.get(i));
                for (int i = 0; i < this.hiddenNodes.size(); i++)
                    this.hiddenNodes.get(i).calculateDelta(this.outputNodes, i, 0);
                for (int i = 0; i < this.outputNodes.size(); i++)
                    this.outputNodes.get(i).updateWeight(this.learningRate);
                for (int i = 0; i < this.hiddenNodes.size(); i++)
                    this.hiddenNodes.get(i).updateWeight(this.learningRate);
            }
            double sum = 0;
            for (Instance instance : this.trainingSet)
                sum += loss(instance);
            sum /= this.trainingSet.size();
            System.out.print("Epoch: " + epoch + ", Loss: ");
            System.out.format("%.3e", sum);
            System.out.println();
        }
    }

    /**
     * Calculate the cross entropy loss from the neural network for a single
     * instance. The parameter is a single instance
     */
    private double loss(Instance instance) {
        // TODO: add code here
        fowardComp(instance);
        double crossEntropy = 0;
        for (int i = 0; i < this.outputNodes.size(); i++) {
            double t = this.outputNodes.get(i).getOutput();
            crossEntropy -= Math.log(t) * instance.classValues.get(i);
        }
        return crossEntropy;
    }

    private void fowardComp(Instance instance) {
        for (int i = 0; i < this.inputNodes.size() - 1; i++)
            this.inputNodes.get(i).setInput(instance.attributes.get(i));
        for (int i = 0; i < this.hiddenNodes.size(); i++) {
            this.hiddenNodes.get(i).getInput();
            this.hiddenNodes.get(i).calculateOutput(null);
        }
        for (int i = 0; i < this.outputNodes.size(); i++)
            this.outputNodes.get(i).getInput();
        for (int i = 0; i < this.outputNodes.size(); i++)
            this.outputNodes.get(i).calculateOutput(this.outputNodes);
    }
}
