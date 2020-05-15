import java.util.*;

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifier implements Classifier {

    private Map<Label, Integer> docsPerLabel;
    private Map<Label, Integer> wordsPerLabel;
    private Map<String, Integer> cntPosWord;
    private Map<String, Integer> cntNegWord;
    private int docsSize;
    private int vocabSize;

    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store
        // them.
        // Then, for all the words in the documents of each label, count the number of
        // occurrences of each word.
        // Save these information as you will need them to calculate the log
        // probabilities later.

        docsSize = trainData.size();
        vocabSize = v;

        wordsPerLabel = getWordsCountPerLabel(trainData);
        docsPerLabel = getDocumentsCountPerLabel(trainData);

        cntPosWord = new HashMap<String, Integer>();
        cntNegWord = new HashMap<String, Integer>();
        for (Instance i : trainData) {
            if (i.label == Label.POSITIVE) {
                for (String word : i.words)
                    if (cntPosWord.containsKey(word))
                        cntPosWord.replace(word, cntPosWord.get(word) + 1);
                    else
                        cntPosWord.put(word, 1);
            } else if (i.label == Label.NEGATIVE) {
                for (String word : i.words)
                    if (cntNegWord.containsKey(word))
                        cntNegWord.replace(word, cntNegWord.get(word) + 1);
                    else
                        cntNegWord.put(word, 1);
            }
        }
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
        int pos = 0, neg = 0;
        for (Instance i : trainData)
            if (i.label == Label.POSITIVE)
                pos += i.words.size();
            else if (i.label == Label.NEGATIVE)
                neg += i.words.size();
        Map<Label, Integer> result = new HashMap<>();
        result.put(Label.POSITIVE, pos);
        result.put(Label.NEGATIVE, neg);
        return result;
    }

    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
        int pos = 0, neg = 0;
        for (Instance i : trainData)
            if (i.label == Label.POSITIVE)
                pos++;
            else if (i.label == Label.NEGATIVE)
                neg++;
        Map<Label, Integer> result = new HashMap<>();
        result.put(Label.POSITIVE, pos);
        result.put(Label.NEGATIVE, neg);
        return result;
    }

    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or
     * P(NEGATIVE)
     */
    private double p_l(Label label) {
        // TODO : Implement
        // Calculate the probability for the label. No smoothing here.
        return docsSize == 0 ? 0 : docsPerLabel.get(label) / (double) docsSize;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label,
     * i.e. P(word|POSITIVE) or P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // TODO : Implement
        // Calculate the probability with Laplace smoothing for word in class(label)
        double cntLabel = 0;
        if (label == Label.POSITIVE)
            cntLabel = cntPosWord.getOrDefault(word, 0);
        else if (label == Label.NEGATIVE)
            cntLabel = cntNegWord.getOrDefault(word, 0);
        double numerator = 0, denominator = 0;
        numerator = cntLabel + 1.0;
        denominator = vocabSize + wordsPerLabel.get(label);
        return denominator == 0 ? 0 : numerator / denominator;
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the
        // probability of the label
        // Set the label to the class with larger log probability
        double logPos = 0, logNeg = 0;
        double condPos = 0, condNeg = 0;
        logPos = (p_l(Label.POSITIVE) == 0 ? 0 : Math.log(p_l(Label.POSITIVE)));
        logNeg = (p_l(Label.NEGATIVE) == 0 ? 0 : Math.log(p_l(Label.NEGATIVE)));
        for (String word : words) {
            condPos += Math.log(p_w_given_l(word, Label.POSITIVE));
            condNeg += Math.log(p_w_given_l(word, Label.NEGATIVE));
        }
        Map<Label, Double> log = new HashMap<Label, Double>();
        log.put(Label.POSITIVE, logPos + condPos);
        log.put(Label.NEGATIVE, logNeg + condNeg);
        ClassifyResult result = new ClassifyResult();
        if (log.get(Label.POSITIVE) < log.get(Label.NEGATIVE))
            result.label = Label.NEGATIVE;
        else
            result.label = Label.POSITIVE;
        result.logProbPerLabel = log;
        return result;
    }
}
