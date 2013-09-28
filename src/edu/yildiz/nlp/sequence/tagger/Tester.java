package edu.yildiz.nlp.sequence.tagger;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.InstanceList;
import edu.yildiz.nlp.sequence.tagger.parsers.WikiLineGroupIterator;

import java.io.File;
import java.util.regex.Pattern;


/**
 * Tester class for sequence classification
 * @author Delip Rao
 *
 */
public class Tester {
  
  private SequenceLearner _learner;
  private SequenceEvaluator  _evaluator;
  
  public Tester(SequenceLearner learner) {
    _learner = learner;
  }
  
  public void loadModel(String modelFileName) throws Exception {
    _learner.loadModel(modelFileName);
  }
  
  public void classify(String testingFileName, OutputCallback outputCallback) throws Exception {
    _learner.classify(testingFileName, outputCallback);
  }

  public void classifyText(String testString, OutputCallback outputCallback) throws Exception {
        _learner.classify(testString, outputCallback);
    }
  public void setEvaluator(SequenceEvaluator evaluator) {
    _evaluator = evaluator;
  }
  
  /**
   * Computes precision, recall and F1
   * @param testingFileName
   * @throws Exception
   */
  public void evaluate(String testingFileName) throws Exception {
    if(_evaluator == null) throw new Exception("call setEvaluator() before evalauate()");
    InstanceList testData = loadTestData(_learner.getInputPipe(), testingFileName);
    _evaluator.evaluateInstanceList(_learner.getModel(), testData);
  }

  private InstanceList loadTestData(Pipe inputPipe, String testFileName) throws Exception {
    InstanceList testData = new InstanceList(inputPipe);
    testData.addThruPipe(
        new WikiLineGroupIterator(new File(testFileName),
          Pattern.compile("^\\s*$"), true));
    return testData;
  }
     /*
    private InstanceList loadTestData(Pipe inputPipe, String testFileName) throws Exception {
       String test="'''Volkan Tokcan''' (d. 11 Ocak 1988 İzmir),Türk\n basketbolcudur. == Kariyeri == İlk kariyerine" ;
        InstanceList testData = new InstanceList(inputPipe);
        testData.addThruPipe(
                new WikiLineGroupIterator(test, Pattern.compile("^\\s*$"), true));
        return testData;
    }
    */
}
