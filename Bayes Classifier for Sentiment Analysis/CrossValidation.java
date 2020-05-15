import java.util.*;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier on training data.
     */
    public static double kFoldScore(Classifier classifier, List<Instance> trainData, int k, int v) {
        // TODO : Implement
        if (k < 2 || k > trainData.size())
            return 0;
        ArrayList<Double> results = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            List<Instance> trainSet = new ArrayList<Instance>();
            List<Instance> testSet = new ArrayList<Instance>();
            for (int j = 0; j < trainData.size(); j++)
                if (j >= (trainData.size() / k * i) && j < trainData.size() / k * (i+1))
                    testSet.add(trainData.get(j));
                else
                    trainSet.add(trainData.get(j));
            classifier.train(trainSet, v);
            int cntCorrect = 0;
            for (Instance j : testSet)
                if (classifier.classify(j.words).label == j.label)
                    cntCorrect++;
            results.add((double)cntCorrect / testSet.size());
        }
        double sum = 0;
        for (Double i : results)
            sum += i;
        return sum / k;
    }
}