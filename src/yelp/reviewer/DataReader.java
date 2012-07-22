/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yelp.reviewer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads review and search inputs from properties file.
 * @author nataraj
 */
public class DataReader {

    Properties prop = new Properties();
    Input reviewsearch ;

    /**
     * 
     * @return Input object with review and search fields set.
     */
    public Input getReviewSearch() {
        return reviewsearch;
    }
    DataReader() {
        
        try {
            prop.load(new FileInputStream("ReviewAndQuery.properties"));
            reviewsearch = new Input(prop.getProperty("Review"), prop.getProperty("Search"));
        } catch (Exception ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        DataReader x = new DataReader();
        System.out.println();
    }
}
