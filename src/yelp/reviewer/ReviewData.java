/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

import java.util.Comparator;

/**
 *
 * @author nataraj
 */
public class ReviewData 
{
    String reviewLine;
    int match;

    ReviewData(String text) 
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

class ReviewDataComparator implements Comparator<ReviewData> 
{
    @Override
    public int compare(ReviewData o1, ReviewData o2) 
    {
        
        ReviewData r1 =  o1;
        ReviewData r2 =  o2;
        return (r1.getMatch() == r2.getMatch() ? 0 : (r1.getMatch() > r2.getMatch() ? -1 : 1)) ;
    }
    
}
