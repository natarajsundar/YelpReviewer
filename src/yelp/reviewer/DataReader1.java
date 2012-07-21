/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nataraj
 */
public class DataReader1 {

    ArrayList<ReviewData> reviewWords;
    public String[] queryWords;
    public Pattern queryPattern;
    public String review;
    public String search;
    public String reviewSummary;
    public DataReader dr;
    final int charLimit = 200;
    public static final String startHighlight = "[[HIGHLIGHT]]";
    public static final String endHighlight = "[[ENDHIGHLIGHT]]";

    public DataReader1() {
        dr = new DataReader();
        review = dr.getReviewsearch().review;
        search = dr.getReviewsearch().searchterm;


    }

    // puts all the pieces together
    public String returnReviewSummary() {
        DataReader1 r1 = new DataReader1();
        parseReviewText();
        parseQuery();
        return findLevelOfMatchForReviewWords();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Summary " + new DataReader1().returnReviewSummary());
    }

    public void parseReviewText() {
        String[] splits = review.split("[-;!?\\.]");
        reviewWords = new ArrayList<ReviewData>();

        for (int i = 0; i < splits.length; i++) {
            reviewWords.add(new ReviewData(splits[i].trim()));
        }
    }

    public void parseQuery() {
        queryWords = search.split("\\s");
        queryPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
    }

    public String findLevelOfMatchForReviewWords() {
        for (ReviewData reviewLine : reviewWords) {
            //perfect match
            Matcher matcher = queryPattern.matcher(reviewLine.getReviewLine());
            if (matcher.matches()) {
                int pos = matcher.start();
                reviewLine.setReviewLine(resizeReviewLineToCharLimit(reviewLine.getReviewLine(), pos, search.length(), charLimit));
                reviewLine.setReviewLine(reviewLine.getReviewLine().replaceAll(search, startHighlight + search + endHighlight));
                reviewLine.setMatch(queryWords.length);
                //break;
            } else {
                //count the no of matches, store pos of lookup in HashMap, set the match level to no of matches for the review line  
                HashMap<String, Integer> searchMap = new HashMap<String, Integer>();
                for (int i = 0; i < queryWords.length; i++) {
                    String searchWord = queryWords[i];
                    int index = reviewLine.getReviewLine().indexOf(searchWord);
                    if (index > -1) {
                        searchMap.put(searchWord, index);

                        //Highlight the search word.
                        reviewLine.setReviewLine((reviewLine.getReviewLine().replaceAll("(?i)" + queryWords[i], startHighlight + queryWords[i] + endHighlight)));
                    }

                }
                reviewLine.setMatch(searchMap.keySet().size());
            }

        }

        // sort the collection based on the no of matches
        Collections.sort(reviewWords, new ReviewDataComparator());

        reviewSummary = "";
        //for the summary to return
        for (ReviewData reviewLine : reviewWords) {
            if (reviewLine.getMatch() > 0 && !reviewLine.getReviewLine().isEmpty()) {
                reviewSummary += reviewLine.getReviewLine() + " ";
            }

        }

        return reviewSummary;
    }

    public String resizeReviewLineToCharLimit(String text, int pos, int length, int maxSize) {
        if (text.length() > maxSize) {
            if (pos + length < maxSize) {
                text = text.substring(0, maxSize - 3) + "...";
            } else {
                int beginIndex = pos - (maxSize / 2) + 3;
                int endIndex = pos + (maxSize / 2) - 3;
                if (endIndex > text.length()) {
                    endIndex = text.length() - 1;
                }

                text = "..." + text.substring(beginIndex, endIndex) + "...";
            }
        }
        return text;
    }

    public String highlightSearchWord(String reviewLine, String searchWord) 
    {
        String inText = reviewLine;
        String word = searchWord;

        Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inText);

        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String replacement = m.group().replace(' ', '~');
            m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(sb);

        String outText = sb.toString();

        System.out.println(outText);
        
        return outText;
    }
}
