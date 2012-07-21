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
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nataraj
 */
public class DataReader {

    Properties prop = new Properties();
    Input reviewsearch ;

    public Input getReviewsearch() {
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
