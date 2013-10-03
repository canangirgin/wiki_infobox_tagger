package edu.yildiz.nlp.sequence.tagger.parsers;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.*;
import edu.yildiz.nlp.sequence.tagger.features.FC;

/**
 * Created with IntelliJ IDEA.
 * User: hs
 * Date: 20.09.2013
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */

    public class Wiki2TokenSequence extends Pipe {
    int errorCount=0;
        public Wiki2TokenSequence() {
            super (new Alphabet(), new LabelAlphabet());
        }

        @Override
        public Instance pipe(Instance carrier) {

            String input = (String) carrier.getData();
            String[] tokenLines = input.split("\n");
            LabelAlphabet labels;
            LabelSequence target = null;
            if (isTargetProcessing())
            {
                labels = (LabelAlphabet)getTargetAlphabet();
                target = new LabelSequence (labels, tokenLines.length);
            }

            TokenSequence data = new TokenSequence(tokenLines.length);
            for(int i=0; i< tokenLines.length; i++){
                String line = tokenLines[i];
             //   System.out.println(line);
                String[] features = line.split("\\s+");
                String label="";
                if (features.length>0)
                {
                    String word = features[0];
                if (word.contains("<ENAMEX_TYPE="))
                {
                    try{
                        //TODO Canan Deneme burayı mutlaka sil!!!!!!!!!!!!!111

                        if (!word.contains("<ENAMEX_TYPE=\"p_dogum_yer\">"))
                        {
                            label = word.substring(0,word.indexOf("\">")+2);
                            if (!label.equals("<ENAMEX_TYPE=\"p_dogum_tar\">") && !label.contains("<ENAMEX_TYPE=\"p_olum_tar\">"))
                            {
                                String hoop="goop";
                            }
                        } else   { label =  "<ENAMEX_TYPE=\"O\">";}

                    word=word.substring(word.indexOf("\">")+2,word.indexOf("</"));
                    }catch(Exception ex)
                    {
                        errorCount++;
                        System.out.println("ERROR-"+line);
                        System.err.println(ex.getStackTrace());
                        continue;
                    }
                } else
                {
                   label =  "<ENAMEX_TYPE=\"O\">";
                }

                String wc = word;
                String bwc = word;
                String dc=word;
                tokenLines[i]=word;
                Token token = new Token(word);
                token.setFeatureValue("W=" + word, 1);
                    tokenLines[i]=originWord;
                //                      transform
                dc = doDigitalCollapse(word);
                if (dc!=null)
                token.setFeatureValue("DC=" + dc, 1);

                wc = doWordClass(wc);
                token.setFeatureValue("WC=" + wc, 1);

                bwc = doBriefWordClass(bwc);
                token.setFeatureValue("BWC=" + bwc, 1);

                String ld = word.toLowerCase();
                token.setFeatureValue("LC=" + ld, 1);

                //                      finish one token line
                if (isTargetProcessing())
                {
                    target.add(label);
                }

                data.add(token);
                }

            }

            carrier.setData(data);
            if (isTargetProcessing())
                carrier.setTarget(target);
            else
                carrier.setTarget(new LabelSequence(getTargetAlphabet()));

            return carrier;
        }

        private String doBriefWordClass(String bwc) {
            bwc = bwc.replaceAll("["+ FC.CAPS+"]+", "A");
            bwc = bwc.replaceAll("["+FC.LOW+"]+", "a");
            bwc = bwc.replaceAll("["+FC.NUM+"]+", "0");
            bwc = bwc.replaceAll("[^"+FC.ALPHANUM+"]+", "x");
            return bwc;
        }

        private String doWordClass(String wc) {
            wc = wc.replaceAll("["+ FC.CAPS+"]", "A");
            wc = wc.replaceAll("["+FC.LOW+"]", "a");
            wc = wc.replaceAll("["+FC.NUM+"]", "0");
            wc = wc.replaceAll("[^"+FC.ALPHANUM+"]", "x");

            return wc;
        }

        private String doDigitalCollapse(String word) {

            if (word.matches("(19|20)\\d\\d"))
                return "<YEAR>";
            else if (word.matches("(19|20)\\d\\ds"))
                return  "<YEARDECADE>";
            else if (word.matches("(19|20)\\d\\d-\\d+"))
                return "<YEARSPAN>";
            else if (word.matches("\\d+\\\\/\\d"))
                return "<FRACTION>";
            else if (word.matches("\\d[\\d,\\.]*"))
                return "<DIGITS>";
            else if (word.matches("(19|20)\\d\\d-\\d\\d-\\d--d"))
                return "<DATELINEDATE>";
            else if (word.matches("(19|20)\\d\\d-\\d\\d-\\d\\d"))
                return "<DATELINEDATE>";

            return null;

        }
}

