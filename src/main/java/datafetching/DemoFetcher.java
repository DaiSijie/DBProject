/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 18, 2015
 */

package datafetching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DemoFetcher extends Fetcher {

    private final Random rand = new Random(1995);
    
    
    private String[][] p = {{"Jose",    "Calderon", "34", "75.34", "120", "10", "ATK"},
                            {"Joel",    "Embid",    "25", "79.21", "150", "2", "DEF"},
                            {"Anthony", "Carmelo",  "30", "78.12", "160", "4","MID"}};

    private String[][] t = {{"New York",     "Knicks", "C1", "D1", "10", "3"},
                            {"Philadelphia", "Sixers", "C1", "D1", "5",  "7"}};
    
    private String[][] pfor = {{"Jose",    "Calderon", "Knicks"},
                               {"Joel",    "Embid",    "Sixers"},
                               {"Anthony", "Carmelo", " Knicks"}};

    private final ArrayList<HashMap<String, String>> playersTuples;
    private final ArrayList<HashMap<String, String>> statsTuples;
    private final ArrayList<HashMap<String, String>> teamTuples;
    private final ArrayList<HashMap<String, String>> playsForTuples;
    
    private final int iterations;

    public DemoFetcher(int iterations){
        this.playersTuples = new ArrayList<HashMap<String, String>>();
        this.statsTuples = new ArrayList<HashMap<String, String>>();
        this.teamTuples = new ArrayList<HashMap<String, String>>();
        this.playsForTuples = new ArrayList<HashMap<String, String>>();
        
        this.iterations = iterations;
    }

    private void fillBasicTuples(){
        //fill players
        for(int i = 0; i < p.length; i++){
            HashMap<String, String> toPut = new HashMap<>();
            toPut.put("FName", p[i][0]);
            toPut.put("LName", p[i][1]);
            toPut.put("Age", p[i][2]);
            toPut.put("Height", p[i][3]);
            toPut.put("Weight", p[i][4]);
            toPut.put("Years_Pro", p[i][5]);
            toPut.put("Position", p[i][6]);
            
            playersTuples.add(toPut);
        }
        
        //fill teams
        for(int i = 0; i < t.length; i++){
          HashMap<String, String> toPut = new HashMap<>();
          toPut.put("Location", t[i][0]);
          toPut.put("TeamName", t[i][1]);
          toPut.put("Conference", t[i][2]);
          toPut.put("Division", t[i][3]);
          toPut.put("Wins", t[i][4]);
          toPut.put("Losses", t[i][5]);
          
          teamTuples.add(toPut);
          
        }
        
        //fill playsfor
        for(int i = 0; i < pfor.length; i++){
            HashMap<String, String> toPut = new HashMap<>();
            toPut.put("FName", pfor[i][0]);
            toPut.put("LName", pfor[i][1]);
            toPut.put("TeamName", pfor[i][2]);
        }
        
        //fills statsTuple
        for(int i = 0; i < p.length; i++){
            HashMap<String, String> toPut = new HashMap<>();
            toPut.put("FName", p[i][0]);
            toPut.put("LName", p[i][1]);
            toPut.put("Games_Played", "0");
            toPut.put("Points", "0");
            toPut.put("Rebounds", "0");
            toPut.put("Assists", "0");
            toPut.put("Steals", "0");
            toPut.put("Blocks", "0");
            toPut.put("Turnovers", "0");
            toPut.put("FGM", "0");
            toPut.put("ThreePFGM", "0");
            toPut.put("FGA", "0");
            toPut.put("ThreePFGA", "0");
            toPut.put("FTM", "0");
            toPut.put("FTA", "0");
            
            statsTuples.add(toPut);
        }
        
    }

    private void addToStats(){
        for(int i = 0; i < p.length; i++){
            statsTuples.get(i).put("Games_Played", ""+ (new Integer(statsTuples.get(i).get("Games_Played")) + rand.nextInt(4)));
            statsTuples.get(i).put("Points", ""+ (new Integer(statsTuples.get(i).get("Points")) + rand.nextInt(30)));
            statsTuples.get(i).put("Rebounds", ""+ (new Integer(statsTuples.get(i).get("Rebounds")) + rand.nextInt(4)));
            statsTuples.get(i).put("Assists", ""+ (new Integer(statsTuples.get(i).get("Assists")) + rand.nextInt(4)));
            statsTuples.get(i).put("Steals", ""+ (new Integer(statsTuples.get(i).get("Steals")) + rand.nextInt(4)));
            statsTuples.get(i).put("Blocks", ""+ (new Integer(statsTuples.get(i).get("Blocks")) + rand.nextInt(4)));
            statsTuples.get(i).put("Turnovers", ""+ (new Integer(statsTuples.get(i).get("Turnovers")) + rand.nextInt(4)));
            statsTuples.get(i).put("FGM", ""+ (new Integer(statsTuples.get(i).get("FGM")) + rand.nextInt(4)));
            statsTuples.get(i).put("ThreePFGM", ""+ (new Integer(statsTuples.get(i).get("ThreePFGM")) + rand.nextInt(4)));
            statsTuples.get(i).put("FGA", ""+ (new Integer(statsTuples.get(i).get("FGA")) + rand.nextInt(4)));
            statsTuples.get(i).put("ThreePFGA", ""+ (new Integer(statsTuples.get(i).get("ThreePFGA")) + rand.nextInt(4)));
            statsTuples.get(i).put("FTM", ""+ (new Integer(statsTuples.get(i).get("FTM")) + rand.nextInt(4)));
            statsTuples.get(i).put("FTA", ""+ (new Integer(statsTuples.get(i).get("FTA")) + rand.nextInt(4)));
        }
    }
    
    @Override
    public void startProcess() {
        fillBasicTuples();
        for(int i = 0; i < iterations; i++){
            simulateWait();
            addToStats();
            simulateWait();
            super.notifyDataWasFetched();
        }
    }
    
    @Override
    public void killProcess() {
        //does nothing
    }

    @Override
    public List<HashMap<String, String>> getPlayersTuples() {
        return playersTuples;
    }

    @Override
    public List<HashMap<String, String>> getStatisticsTuples() {
        return statsTuples;
    }

    @Override
    public List<HashMap<String, String>> getTeamTuples() {
        return teamTuples;
    }

    @Override
    public List<HashMap<String, String>> getPlaysForTuples() {
        return playsForTuples;
    }

    private void simulateWait(){
        try {
            System.out.print("Data fetching...");
            Thread.sleep(500);
            System.out.println("... done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
