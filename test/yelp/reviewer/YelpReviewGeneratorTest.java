/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nataraj
 */
public class YelpReviewGeneratorTest 
{
    
    public YelpReviewGeneratorTest() {
    }

  
    /**
     * Test of highlight_doc method, of class YelpReviewGenerator.
     */
    @Test
    public void testHighlight_doc() 
    {
        System.out.println("Testing highlight_doc");
        String doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        String query = "deep dish pizza";
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String expResult = "Little star's [[HIGHLIGHT]]deep dish pizza[[ENDHIGHLIGHT]] sure is fantastic ";
        String result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);
        
        doc = "deep dish pizza";
        query = "deep dish pizza";
        instance = new YelpReviewGenerator();
        expResult = "[[HIGHLIGHT]]deep dish pizza[[ENDHIGHLIGHT]] ";
        result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);        
        
        doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        query = "dish pizza";
        instance = new YelpReviewGenerator();
        expResult = "Little star's deep [[HIGHLIGHT]]dish pizza[[ENDHIGHLIGHT]] sure is fantastic ";
        result = instance.highlight_doc(doc, query);//
        
        doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        query = "pizza";
        instance = new YelpReviewGenerator();
        expResult = "Little star's deep dish [[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]] sure is fantastic ";
        result = instance.highlight_doc(doc, query);
        
        doc = "You want real New York/New Jersey style pizza in the East Bay?The only place able to deliver the real deal is Arinel Pizza in Berkeley. It is a hole in the wall, and it is not the real Rays on 7th in Manhattan (also a hole in the wall), but it is solid, good quality east coast pizza, honestly. After living in CA for 29 years, coming from New Jersey, this is the only pizza that reminds me of the halcyon days of my youth.";
        query = "new york pizza";
        instance = new YelpReviewGenerator();
        expResult = "You want real [[HIGHLIGHT]]New York[[ENDHIGHLIGHT]]/[[HIGHLIGHT]]New[[ENDHIGHLIGHT]] Jersey style [[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]] in the East Bay After living in CA for 29 years, coming from [[HIGHLIGHT]]New[[ENDHIGHLIGHT]] Jersey, this is the only [[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]] that reminds me of the halcyon days of my youth The only place able to deliver the real deal is Arinel [[HIGHLIGHT]]Pizza[[ENDHIGHLIGHT]] in Berkeley It is a hole in the wall, and it is not the real Rays on 7th in Manhattan (also a hole in the wall), but it is solid, good quality east coast [[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]], honestly ";
        result = instance.highlight_doc(doc, query);//
        
        
        
        doc = null;
        query = "deep dish pizza";
        instance = new YelpReviewGenerator();
        expResult = "Review body can't be null/empty";
        result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);
        
        doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        query = null;
        instance = new YelpReviewGenerator();
        expResult = "Query can't be null/empty";
        result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);
        
        doc = "";
        query = "deep dish pizza";
        instance = new YelpReviewGenerator();
        expResult = "Review body can't be null/empty";
        result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);
        
        doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        query = "";
        instance = new YelpReviewGenerator();
        expResult = "Query can't be null/empty";
        result = instance.highlight_doc(doc, query);
        assertEquals(expResult, result);

    }

     /**
     * Test of parseReviewText method, of class YelpReviewGenerator.
     */
    @Test
    public void testParseReviewText() 
    {
        System.out.println("parseReviewText");
        String doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        String query = "deep dish pizza";
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String result = instance.highlight_doc(doc, query);
        instance.parseReviewText();
        assert (!instance.linesInReview.isEmpty());
        assert (instance.linesInReview.size() == 3);
        
    }

    /**
     * Test of parseQuery method, of class YelpReviewGenerator.
     */
    @Test
    public void testParseQuery() 
    {
        System.out.println("parseQuery");
        String doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        String query = "deep dish pizza";
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String result = instance.highlight_doc(doc, query);
        instance.parseQuery();
        assert (instance.queryWords.length != 0 );
        assert (instance.queryPattern != null);
    }

    /**
     * Test of matchReviewWordsReturnSummary method, of class YelpReviewGenerator.
     */
    @Test
    public void testMatchReviewWordsReturnSummary() 
    {
        System.out.println("matchReviewWordsReturnSummary");
       
        System.out.println("parseQuery");
        String doc = "I like fish. Little star's deep dish pizza sure is fantastic.Dogs are funny.";
        String query = "deep dish pizza";
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String result = instance.highlight_doc(doc, query);
        String expResult = instance.matchReviewWordsReturnSummary();
        assert (expResult != null);
        assert (expResult.contains("deep dish pizza"));
    }

    /**
     * Test of resizeReviewLineToCharLimit method, of class YelpReviewGenerator.
     */
    @Test
    public void testResizeReviewLineToCharLimit() {
        System.out.println("resizeReviewLineToCharLimit");
        String text = "and we enjoyed the delicious garlic bread, a Caprese Salad (the cheese, tomatoes and basil were AMAZING with that balsamic), and Mediterranean Chicken Deep Dish Pizza";
        int pos = 0;
        int length = 15;
        int maxSize = 200;
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String expResult = "and we enjoyed the delicious garlic bread, a Caprese Salad (the cheese, tomatoes and basil were AMAZING with that balsamic), and Mediterranean Chicken Deep Dish Pizza";
        String result = instance.resizeReviewLineToCharLimit(text, pos, length, maxSize);
        assertEquals(expResult, result);
      }

    /**
     * Test of highlightSearchWord method, of class YelpReviewGenerator.
     */
    @Test
    public void testHighlightSearchWord() 
    {
        System.out.println("highlightSearchWord");
        String reviewLine = "This is my favorite pizza spot in SF";
        String searchWord = "pizza";
        YelpReviewGenerator instance = new YelpReviewGenerator();
        String expResult = "This is my favorite [[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]] spot in SF";
        String result = instance.highlightSearchWord(reviewLine, searchWord);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

   
}
