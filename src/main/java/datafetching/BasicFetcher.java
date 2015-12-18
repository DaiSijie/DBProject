package datafetching;

import java.util.HashMap;
import java.util.List;

public class BasicFetcher extends Fetcher {

    /*
     * PUT HERE ALL DATASTRUCTURES CONTAINING JAVA VARIABLE FROM LAST FETCH
     */
    
    public BasicFetcher(){
        
    }
    
    /*
     * TODO: (Niki)
     *        - find a way to periodically run the following
     *        - fetch data smartly from web-site
     *        - put fetched data in the datastructure
     *        - call super.notifyDataWasFetched() when it's done
     */
    
    @Override
    public void startProcess() {
        //Note: this is an idea
        
    }

    @Override
    public void killProcess() {
        //Note: this is an idea
        
    }

    @Override
    public List<HashMap<String, String>> getPlayersTuples() {
        // TODO Auto-generated method stub
        //if you don't know what names to put in the map, take a look at the enums in the SQLStorer class
        return null;
    }

    @Override
    public List<HashMap<String, String>> getStatisticsTuples() {
        // TODO Auto-generated method stub
        //if you don't know what names to put in the map, take a look at the enums in the SQLStorer class
        return null;
    }

    @Override
    public List<HashMap<String, String>> getTeamTuples() {
        // TODO Auto-generated method stub
        //if you don't know what names to put in the map, take a look at the enums in the SQLStorer class
        return null;
    }

    @Override
    public List<HashMap<String, String>> getPlaysForTuples() {
        // TODO Auto-generated method stub
        //if you don't know what names to put in the map, take a look at the enums in the SQLStorer class
        return null;
    }
    
}
