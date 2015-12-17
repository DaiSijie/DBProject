package datafetching;

import java.util.ArrayList;
import java.util.List;

import datastoring.DataStorer;

public abstract class Fetcher {

    private final List<DataStorer> observers = new ArrayList<>();
    
    public void  registerDataStorer(DataStorer ds){
        this.observers.add(ds);
    }
    
    public void notifyDataWasFetched(){
        for(DataStorer ds : observers){
            ds.notifyDataWasFetched();
        }
    }
    
    public abstract void startProcess();
    
    public abstract void killProcess();
    
}
