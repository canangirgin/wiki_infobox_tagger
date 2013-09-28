package edu.yildiz.nlp.sequence.tagger;

import cc.mallet.types.Sequence;

/**
 * 
 * @author Delip Rao
 *
 */
public class WikiOutputCallback implements OutputCallback {

  @SuppressWarnings("unchecked")
  public void process(String[] input, Sequence output) {

    for(int i = 0; i < output.size(); i++) {
        StringBuffer buf = new StringBuffer();
        buf.append(input[i]).append(" ");
        buf.append(output.get(i));
        if (output.get(i)!= "<ENAMEX_TYPE=\"O\">")
        CRFSequenceLearner.resultSet.add(new ResultSet(""+input[i],""+output.get(i)));
        System.out.println(buf.toString());
    }

    System.out.println("");
  }
    @SuppressWarnings("unchecked")
    public void process(Sequence input, Sequence output) {
        // assert input.size() == output.size()
        for(int i = 0; i < input.size(); i++) {
            System.out.println(output.get(i));
            //FeatureVector fv = (FeatureVector)input.get(i);
            //System.out.println(" " + fv.toString(true));
        }
        System.out.println("");
    }

}
