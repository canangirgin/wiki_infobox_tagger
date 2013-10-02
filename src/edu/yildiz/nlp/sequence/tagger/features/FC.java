package edu.yildiz.nlp.sequence.tagger.features;

/**
 * A listing of feature constants
 * @author Delip Rao
 *
 */
public class FC {
  
  private FC() {}
  
  public static final String CAPS = "[A-ZÜİÇĞÖ]";
  public static final String LOW = "[a-zsüçğö]";
  public static final String CAPSNUM = "[A-Z0-9ÜİÇĞÖ]";
  public static final String ALPHA = "[A-Za-zsüçğöÜİÇĞÖ]";
  public static final String ALPHANUM = "[A-Za-z0-9süçğöÜİÇĞÖ]";
  public static final String NUM = "[0-9]";
  public static final String PUNT = "[,\\.;:?!]";
  public static final String QUOTE = "[\"`']";

}
