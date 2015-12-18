package datafetching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import datastoring.DataStorer;

public abstract class Fetcher {

    private final List<DataStorer> observers = new ArrayList<>();
    
    public void  registerDataStorer(DataStorer ds){
        this.observers.add(ds);
    }
    
    public void notifyDataWasFetched(){
        for(DataStorer ds : observers){
            ds.notifyDataWasFetched(this);
        }
    }
    
    public abstract void startProcess();
    
    public abstract void killProcess();
    
    
    /**
     * @return A map with key, value. (e.g FName and John) - Will only be called once
     */
    public abstract List<HashMap<String, String>> getPlayersTuples();
    
    /**
     * Will always be called
     */
    public abstract List<HashMap<String, String>> getStatisticsTuples();
    
    /**
     * Will only be called once
     */
    public abstract List<HashMap<String, String>> getTeamTuples();
    
    
    /**
     * Will only be called once
     */
    public abstract List<HashMap<String, String>> getPlaysForTuples();
    
    
}
