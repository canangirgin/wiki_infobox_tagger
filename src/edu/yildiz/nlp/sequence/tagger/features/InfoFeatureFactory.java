package edu.yildiz.nlp.sequence.tagger.features;

import cc.mallet.pipe.*;
import cc.mallet.pipe.tsf.*;
import edu.yildiz.nlp.sequence.tagger.parsers.Wiki2TokenSequence;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: a
 * Date: 8/23/13
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfoFeatureFactory extends CompositeFeatureFactory {


    public void init() {
        _featurePipeline.rebuild();
        // convert strings to tokens

    }

    public void build() {
        init();
        buildSpecific();

    }

    public void buildSpecific(){
        _featurePipeline.addPipes(new Pipe [] {
        //Inputları alıp featureları ayırıyor.
        //new SimpleTaggerSentence2TokenSequence(),
         new Wiki2TokenSequence(),
        //(YIS-CRF-01)
        new RegexMatches("STARTCAPITALIZE", Pattern.compile("^"+FC.CAPS+".*")),
        //(YIS-CRF-02)
        new RegexMatches("STARTNONUM", Pattern.compile("^[^\\d].*")),
        //(YIS-CRF-03)
        new RegexMatches("HASLESSTHREELETTER", Pattern.compile("([A-Z]|[a-z]){3}")),
        //(YIS-CRF-04)
        new RegexMatches("STARTCAPANDHASOPPOS", Pattern.compile("^[A-Z].+'([A-Z]|[a-z])+")),
                //   Has bracket
        new RegexMatches("HASBRACKET", Pattern.compile ("[()]")),
        new InBracket("INBRACKET",true) ,
        //Sadece sayılardan mı oluşuyor?
        new RegexMatches("ALLDIGITS", Pattern.compile ("[0-9]")),
        new RegexMatches("HASDIGIT", Pattern.compile(".*[0-9].*")),


                new RegexMatches("INITCAP", Pattern.compile (FC.CAPS+".*")),
                new RegexMatches("CAPITALIZED", Pattern.compile (FC.CAPS+FC.LOW+"*")),
                new RegexMatches("ALLCAPS", Pattern.compile (FC.CAPS+"+")),
                new RegexMatches("MIXEDCAPS", Pattern.compile ("[A-Z][a-z]+[A-Z][A-Za-z]*")),
                new RegexMatches("CONTAINSDIGITS", Pattern.compile (".*[0-9].*")),
                new RegexMatches("SINGLEDIGITS", Pattern.compile ("[0-9]")),
                new RegexMatches("DOUBLEDIGITS", Pattern.compile ("[0-9][0-9]")),
                new RegexMatches("NUMERICAL", Pattern.compile ("[-0-9]+[\\.,]+[0-9\\.,]+")),
                new RegexMatches("ALPHNUMERIC", Pattern.compile ("[A-Za-z0-9]+")),
                new RegexMatches("ROMAN", Pattern.compile ("[ivxdlcm]+|[IVXDLCM]+")),
                new RegexMatches("MULTIDOTS", Pattern.compile ("\\.\\.+")),
                new RegexMatches("ENDSINDOT", Pattern.compile ("[^\\.]+.*\\.")),
                new RegexMatches("CONTAINSDASH", Pattern.compile(FC.ALPHANUM+"+-"+FC.ALPHANUM+"*")),
                new RegexMatches("ACRO", Pattern.compile ("[A-Z][A-Z\\.]*\\.[A-Z\\.]*")),
                new RegexMatches("LONELYINITIAL", Pattern.compile (FC.CAPS+"\\.")),
                new RegexMatches("SINGLECHAR", Pattern.compile (FC.ALPHA)),
                new RegexMatches("CAPLETTER", Pattern.compile ("[A-Z]")),
                new RegexMatches("PUNC", Pattern.compile (FC.PUNT)),
                new RegexMatches("QUOTE", Pattern.compile (FC.QUOTE)),
                new RegexMatches("STARTDASH", Pattern.compile ("-.*")),
                new RegexMatches("ENDDASH", Pattern.compile (".*-")),
                new RegexMatches("FORWARDSLASH", Pattern.compile ("/")),
                new RegexMatches("ISBRACKET", Pattern.compile ("[()]")),
               new RegexMatches("PUNCTUATION", Pattern.compile(".*[,.;:?!-+].*")),
                new RegexMatches("ISPUNCT", Pattern.compile("[`~!@#$%^&*()-=_+\\[\\]\\\\{}|;\':\\\",./<>?]+")),
              new RegexMatches("HAS_QUOTE", Pattern.compile(".*'.*")),
                new RegexMatches("HAS_SLASH", Pattern.compile(".*/.*")),
                new RegexMatches("START_MINUS", Pattern.compile("-.*")),
                new RegexMatches("START_PLUS", Pattern.compile("\\+.*")),
                new RegexMatches("END_PERCENT", Pattern.compile(".*%")),
                  /* Make the word a feature. */


                new TokenSequenceLowercase(),
                new TokenText("WORD="),

                new TokenTextCharSuffix("SUFFIX2=",2),
                new TokenTextCharSuffix("SUFFIX3=",3),
                new TokenTextCharSuffix("SUFFIX4=",4),
                new TokenTextCharPrefix("PREFIX2=",2),
                new TokenTextCharPrefix("PREFIX3=",3),
                new TokenTextCharPrefix("PREFIX4=",4),
                new TokenTextCharNGrams("CHARNGRAM=", new int[] {2,3,4}),

                  /* FeatureInWindow features. */
                //Burayı duşun
                /*
                new FeaturesInWindow("WINDOW=",-5,5,
                Pattern.compile("WORD=.*|SUFFIX.*|PREFIX.*|DC=.*|WC=.*|BWC=.*|[A-Z]+"),true), */
                new FeaturesInWindow("WINDOW=",-5,3,
                        Pattern.compile("WORD=.*|SUFFIX.*|PREFIX.*|DC=.*|WC=.*|BWC=.*"),true),

        /*
        int[][] conjunctions = new int[2][];
        conjunctions[0] = new int[] { -2,-1 };
        conjunctions[1] = new int[] { 1, 2};     */
                new OffsetConjunctions(true,Pattern.compile("WORD=.*"),
                        new int[][]{{-1},{1},{-1,0}})  ,





                //Inputları ve targetları yazdırıyor.
                new PrintInputAndTarget(),
                new PrintTokenSequenceFeatures()
        });

    }
    @Override
    public Pipe getPipe() {

       //return getPipe(new SimpleTagger.SimpleTaggerSentence2FeatureVectorSequence());
        return getPipe(new TokenSequence2FeatureVectorSequence());

    }
}
