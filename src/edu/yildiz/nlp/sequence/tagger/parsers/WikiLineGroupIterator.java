
package edu.yildiz.nlp.sequence.tagger.parsers;


import cc.mallet.types.Instance;
import edu.yildiz.nlp.sequence.tagger.CRFSequenceLearner;

import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.regex.Pattern;

/** Iterate over groups of lines of text, separated by lines that
 match a regular expression.  For example, the WSJ BaseNP data
 consists of sentences with one word per line, each sentence
 separated by a blank line.  If the "boundary" line is to be
 included in the group, it is placed at the end of the group. */

public class WikiLineGroupIterator implements Iterator<Instance>
{
    LineNumberReader reader;
    Pattern lineBoundaryRegex;
    boolean skipBoundary;
    //boolean putBoundaryLineAtEnd; // Not yet implemented
    String nextLineGroup;
    String nextBoundary;
    String nextNextBoundary;
    int groupIndex = 0;
    boolean putBoundaryInSource = true;
    String[] inputArray;
    int i=0;

    public WikiLineGroupIterator (String input, Pattern lineBoundaryRegex, boolean skipBoundary)
    {
        inputArray=input.split(" ");
        CRFSequenceLearner.inputArray= new  String[input.split("\n").length][];
        this.lineBoundaryRegex = lineBoundaryRegex;
        this.skipBoundary = skipBoundary;
        setNextLineGroup();
    }

    public String peekLineGroup () {
        return nextLineGroup;
    }

    private void setNextLineGroup ()
    {
        StringBuffer sb = new StringBuffer ();
        String line;
        int k=0;
        if (!skipBoundary && nextBoundary != null)
            sb.append(nextBoundary + '\n');
        while(i<inputArray.length) {

              line = inputArray[i];
              i++;
            if (line == null) {
                break;
            } else if (lineBoundaryRegex.matcher (line).matches()) {
                if (sb.length() > 0) {
                    this.nextBoundary = this.nextNextBoundary;
                    this.nextNextBoundary = line;
                    break;
                } else { // The first line of the file.
                    if (!skipBoundary)
                    {
                        sb.append(line + '\n');

                        k++;
                    }
                    this.nextNextBoundary = line;
                }
            } else {
                sb.append(line);
                sb.append('\n');
                k++;

            }
        }
        CRFSequenceLearner.inputArray[groupIndex]=sb.toString().split("\n");
        if (sb.length() == 0)
            this.nextLineGroup = null;
        else
            this.nextLineGroup = sb.toString();

    }


    public Instance next ()
    {
        assert (nextLineGroup != null);
        Instance carrier = new Instance (nextLineGroup, null, "linegroup"+groupIndex++,
                putBoundaryInSource ? nextBoundary : null);
        setNextLineGroup ();
        return carrier;
    }

    public boolean hasNext ()	{	return nextLineGroup != null;	}

    public void remove () {
        throw new IllegalStateException ("This Iterator<Instance> does not support remove().");
    }

}
