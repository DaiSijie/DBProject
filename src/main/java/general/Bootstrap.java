/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 17, 2015
 */

package general;

import datafetching.BasicFetcher;
import datafetching.DemoFetcher;
import datafetching.Fetcher;
import datastoring.DataStorer;
import datastoring.SQLStorer;

public class Bootstrap {

    public static void main(String[] args){
        System.out.println("Hello world!");
        
        Fetcher fetcher = new DemoFetcher(10);
        DataStorer sqlStorer = new SQLStorer();
        
        fetcher.registerDataStorer(sqlStorer);
        
        fetcher.startProcess();
        /*
         * grabs data and stores it...
         */
        fetcher.killProcess();
        
        //show data :-)
    }
    
}
