/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

/**
 *Input object to store the review and the search params.
 * @author nataraj
 */
public class Input 
{
    String review;
    String searchterm;

    public String getReview() {
        return review;
    }

    public Input(String review, String searchterm) {
        this.review = review;
        this.searchterm = searchterm;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSearchterm() {
        return searchterm;
    }

    public void setSearchterm(String searchterm) {
        this.searchterm = searchterm;
    }
    
}
