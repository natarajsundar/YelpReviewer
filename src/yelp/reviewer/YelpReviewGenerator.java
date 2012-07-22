
package yelp.reviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class, contains the functional part which matches and returns review summary with highlights.  
 * @author nataraj
 */
public class YelpReviewGenerator {

    /**
     * Stores parsed out lines from the review
     */
    ArrayList<ReviewUnit> linesInReview;
   /**
     * Stores parsed out words from the search query
     */
    
    public String[] queryWords;
    /**
     * Java.pattern from the search query which we can use to match with lines from the review.
     */
    
    public Pattern queryPattern;
    
    /**
     * The review as is.
     */
    
    public String review;
    
    /**
     * The search query as is.
     */
    
    public String search;
    
    /**
     *The result that we return, summary of the review with search query matches highlighted.
     */
    
    public String reviewSummary;
    
    /**
     * This object reads inputs(review,search params) from our properties file, stores the data in a Input object and passes it to our ReviewGenerator.
     */
    
    
    public DataReader dr;
    
    /**
     * The limit on the no of words for each line of the summary
     */
    
    final int charLimit = 200;
    
    public static final String startHighlight = "[[HIGHLIGHT]]";
    
    public static final String endHighlight = "[[ENDHIGHLIGHT]]";

    /**
     * 
     * @param reviewinput  the full input review
     * @param searchquery  the input search query
     */
    public YelpReviewGenerator(String reviewinput, String searchquery) {
        this.review = reviewinput;
        this.search = searchquery;
    }

    /**
     * default Constructor
     */
    public YelpReviewGenerator() {
    }

    // puts all the pieces together
    /**
     * 
     * @param doc  review
     * @param query  search params
     * @return  review summary
     */
    public String highlight_doc(String doc, String query) {
        if (doc == null || doc.isEmpty()) {
            return "Review body can't be null/empty";
        } else {
            this.review = doc;
        }
        if (query == null || query.isEmpty()) 
        {
            return "Query can't be null/empty";
            
        } else {
            this.search = query;
        }
        parseReviewText();
        parseQuery();
        return matchReviewWordsReturnSummary();
    }

    /**
     *  Main() function to make the project executable, contains a couple of samples.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataReader drmain = new DataReader();
        System.out.println("Review Summary : " + new YelpReviewGenerator().highlight_doc(drmain.getReviewSearch().review, drmain.getReviewSearch().searchterm));
        //System.out.println("Review Summary : " + new YelpReviewGenerator().highlight_doc("I like fish. Little star's deep dish pizza sure is fantastic. Dogs are funny.", "deep dish pizza"));
    }

    /**
     * Parses the review into separate lines based on the regex.
     */
    public void parseReviewText() {
        String[] splits = review.split("[-;!?\\.]");
        linesInReview = new ArrayList<ReviewUnit>();

        for (int i = 0; i < splits.length; i++) {
            linesInReview.add(new ReviewUnit(splits[i].trim()));
        }
    }

    /**
     * Parses the search params into words and a pattern that we can use to look up matches.
     */
    public void parseQuery() {
        queryWords = search.split("\\s");
        queryPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
    }

    /**
     * 1) Check if lines parsed out from the review have words from the query.
     * 2) If a word is found in a line highlight it.
     * 3) Sort the lines based on the no of matches they have.
     * 4) Append sorted lines to the the reviewSummary. 
     * 5) If two consecutive words are highlight then extend the highlight to cover both. 
     * @return Review Summary
     */
    public String matchReviewWordsReturnSummary() {
        for (ReviewUnit reviewLine : linesInReview) {
            //check if the review is a perfect match
            Matcher matcher = queryPattern.matcher(reviewLine.getReviewLine());
            if (matcher.matches()) {
                int pos = matcher.start();
                highlightSearchWord(reviewLine.getReviewLine(), search);
                reviewLine.setReviewLine(resizeReviewLineToCharLimit(reviewLine.getReviewLine(), pos, search.length(), charLimit));
                reviewLine.setReviewLine(highlightSearchWord(reviewLine.getReviewLine(), search));
                reviewLine.setMatch(queryWords.length);

            } else {
                //count the no of matches, store pos of lookup in HashMap, set the match level to no of matches for the review line  
                HashMap<String, Integer> searchMap = new HashMap<String, Integer>();
                for (int i = 0; i < queryWords.length; i++) {
                    String searchWord = queryWords[i];
                    int index = reviewLine.getReviewLine().toLowerCase().indexOf(searchWord.toLowerCase());
                    if (index > -1) {
                        searchMap.put(searchWord, index);

                        //Highlight the search word.
                        reviewLine.setReviewLine(highlightSearchWord(reviewLine.getReviewLine(), searchWord));
                    }

                }
                reviewLine.setMatch(searchMap.keySet().size());
            }

        }

        // sort the collection based on the no of matches in descending order
        Collections.sort(linesInReview, new ReviewDataComparator());

        reviewSummary = "";
        //prepare the summary by appendin the sorted lines which have words from the query
        for (ReviewUnit reviewLine : linesInReview) {
            if (reviewLine.getMatch() > 0 && !reviewLine.getReviewLine().isEmpty()) {
                reviewSummary += reviewLine.getReviewLine() + " ";
            }

        }
        //make highlights continuous 
        reviewSummary = reviewSummary.replaceAll("\\[\\[ENDHIGHLIGHT\\]\\] \\[\\[HIGHLIGHT\\]\\]", " ");
        return reviewSummary;
    }

    /**
     * Resize review line to be within the limit
     * @param text  Input line
     * @param pos  position where a match was found for the search param
     * @param length  length of the search param
     * @param maxSize  the limit on the no of characters, if you exceed limit use ellipses 
     * @return Review line re-sized
     */
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

    /**
     * Highlight search term in the given review line
     * @param reviewLine 
     * @param searchWord
     * @return Review Line with the search term highlighted
     */
    public String highlightSearchWord(String reviewLine, String searchWord) {
        String inText = reviewLine.trim();
        String word = searchWord.trim();

        Pattern p = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inText);

        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String replacement = (m.group().replace(m.group(), startHighlight + m.group().trim() + endHighlight)).trim();
            m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(sb);

        return sb.toString();
    }

}
