import java.util.*;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0)
			this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(this.trainData, 0);
	}

	private DecTreeNode buildTree(List<List<Integer>> dataset, int curDepth) {
		DecTreeNode node = null;
		int cntOne = 0, cntZero = 0;
		for (List<Integer> i : dataset)
			if (i.get(i.size() - 1) == 0)
				cntZero++;
		cntOne = dataset.size() - cntZero;
		if (cntZero == 0) {
			node = new DecTreeNode(1, -1, -1);
		} else if (cntOne == 0) {
			node = new DecTreeNode(0, -1, -1);
		} else if (curDepth >= this.maxDepth) {
			if (cntOne >= cntZero)
				node = new DecTreeNode(1, -1, -1);
			else
				node = new DecTreeNode(0, -1, -1);
		} else if (dataset.size() <= this.maxPerLeaf) {
			if (cntOne >= cntZero)
				node = new DecTreeNode(1, -1, -1);
			else
				node = new DecTreeNode(0, -1, -1);
		} else {
			pairAttr bestAttrPair = bestPair(dataset);
			if (bestAttrPair == null) {
				if (cntOne >= cntZero)
					node = new DecTreeNode(1, -1, -1);
				else
					node = new DecTreeNode(0, -1, -1);
			} else {
				List<List<Integer>> leftData = new ArrayList<>();
				List<List<Integer>> rightData = new ArrayList<>();
				for (List<Integer> i : dataset) {
					if (i.get(bestAttrPair.attr) <= bestAttrPair.threshold)
						leftData.add(i);
					else
						rightData.add(i);
				}
				node = new DecTreeNode(-1, bestAttrPair.attr, bestAttrPair.threshold);		
				node.left = buildTree(leftData, curDepth + 1);
				node.right = buildTree(rightData, curDepth + 1);
			}
		}
		return node;
	}

	public int classify(List<Integer> instance) {
		// Note that the last element of the array is the label.
		DecTreeNode curNode = this.root;
		while (curNode.isLeaf() == false) {
			if (instance.get(curNode.attribute) <= curNode.threshold)
				curNode = curNode.left;
			else
				curNode = curNode.right;
		}
		return curNode.classLabel;
	}

	private pairAttr bestPair(List<List<Integer>> dataset) {
		double maxinfoGain = -1;
		double infoGain = -1;
		double initEntropy = totEntropy(dataset);
		pairAttr bestPair = null;
		for (int attr = 0; attr < this.numAttr; attr++) {
			for (int threshold = 1; threshold <= 10; threshold++) {
				pairAttr pair = new pairAttr(attr, threshold);
				infoGain = infoGainPer(pair, dataset, initEntropy);
				if (infoGain > maxinfoGain) {
					bestPair = pair;
					maxinfoGain = infoGain;
				}
			}
		}
		if (bestPair == null)
			return null;
		else
			return bestPair;
	}

	private double infoGainPer(pairAttr pairAttr, List<List<Integer>> dataset, double initEntropy) {
		int leftZero = 0, leftOne = 0;
		int rightZero = 0, rightOne = 0;
		int sum = 0;
		for (List<Integer> i : dataset) {
			if (i.get(pairAttr.attr) <= pairAttr.threshold) {
				if (i.get(i.size() - 1) == 1)
					leftOne++;
				else
					leftZero++;
			} else if (i.get(pairAttr.attr) > pairAttr.threshold) {
				if (i.get(i.size() - 1) == 1)
					rightOne++;
				else
					rightZero++;
			}
		}
		double leftEntropy = calEntropy(leftZero, leftOne);
		double rightEntropy = calEntropy(rightZero, rightOne);
		sum = leftZero + leftOne + rightZero + rightOne;
		return initEntropy - ((double) (leftZero + leftOne) / sum * leftEntropy
				+ (double) (rightZero + rightOne) / sum * rightEntropy);
	}

	private double totEntropy(List<List<Integer>> dataset) {
		int cntZero = 0, cntOne = 0;
		for (List<Integer> i : dataset)
			if (i.get(i.size() - 1) == 0)
				cntZero++;
		cntOne = dataset.size() - cntZero;
		return calEntropy(cntZero, cntOne);
	}

	private double calEntropy(int cntZero, int cntOne) {
		double prZero = 0, prZeroLog = 0;
		double prOne = 0, prOneLog = 0;
		if (cntZero != 0) {
			prZero = cntZero / ((double) (cntZero + cntOne));
			prZeroLog = Math.log(prZero) / Math.log(2);
		}
		if (cntOne != 0) {
			prOne = cntOne / ((double) (cntZero + cntOne));
			prOneLog = Math.log(prOne) / Math.log(2);
		}
		return -(prZero * prZeroLog + prOne * prOneLog);
	}

	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if (node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		} else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if (node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		} else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}

	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i++) {
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual * 100.0 / (double) numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}

class pairAttr {
	public int attr;
	public int threshold;
	public pairAttr(int attr, int th) {
		this.attr = attr;
		this.threshold = th;
	}
}