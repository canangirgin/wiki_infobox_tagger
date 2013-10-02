package edu.yildiz.nlp.sequence.tagger;

import cc.mallet.types.Sequence;

/**
 * 
 * @author Delip Rao
 *
 */
public class WikiOutputCallback implements OutputCallback {

  @SuppressWarnings("unchecked")
  public void process(Sequence output) {

      for(int i = 0; i < output.size(); i++) {

          if (!output.get(i).equals("<ENAMEX_TYPE=\"O\">"))
          {
              CRFSequenceLearner.resultSet.add(new ResultSet(CRFSequenceLearner.inputArrayDeneme[CRFSequenceLearner.outCount], ""+output.get(i)));
          }
          CRFSequenceLearner.outCount++;
      }

      System.out.println("");
  }

    @Override
    public void print() {
        for(ResultSet result:CRFSequenceLearner.resultSet)
        {
            StringBuffer buf = new StringBuffer();
            buf.append(result.word+ "\t\t"+result.label);
            System.out.println(buf.toString());
        }
    }

}