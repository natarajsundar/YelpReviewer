/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

import java.util.Comparator;


/**
 * Represents a single line parsed from the review, also contains a field called match which indicates the level of match.
 * @author nataraj
 */
public class ReviewUnit 
{
    String reviewLine;
    int match;

    ReviewUnit(String text) 
    {
        this.reviewLine=text;
        match=0;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }
    
    public String getReviewLine() {
        return reviewLine;
    }

    public void setReviewLine(String reviewLine) {
        this.reviewLine = reviewLine;
    }
    
}
class ReviewDataComparator implements Comparator<ReviewUnit> 
{
    @Override
    public int compare(ReviewUnit o1, ReviewUnit o2) 
    {
        
        ReviewUnit r1 =  o1;
        ReviewUnit r2 =  o2;
        return (r1.getMatch() == r2.getMatch() ? 0 : (r1.getMatch() > r2.getMatch() ? -1 : 1)) ;
    }
    
}
