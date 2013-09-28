package edu.yildiz.nlp.sequence.tagger;

import cc.mallet.types.Sequence;

/**
 * 
 * @author Delip Rao
 *
 */
public interface OutputCallback {
  @SuppressWarnings("unchecked")
  public void process(String[] input, Sequence output);
  public void process(Sequence input, Sequence output);
}
