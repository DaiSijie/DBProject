package datastoring;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

import datafetching.Fetcher;

public class SQLStorer implements DataStorer{

    private static final String DIRECTORY_NAME = "data";
    private static final String FILE_NAME = "db.sql";
    private static final Sql2o database = prepareCuteDatabase("");
    
    @Override
    public void notifyDataWasFetched(Fetcher f) {
        if(!basicAlreadyFilled())
            fillBasics(f);
        fillStatistics(f, getCurrentTimeStep() + 1);
    }
    
    private void fillBasics(Fetcher f){
        Connection conn = database.open();
        
        //Fill players
        for(HashMap<String, String> tuple : f.getPlayersTuples()){
            String cmd = "INSERT INTO Player Values(:FName, :LName, :Age, :Height, "+
                ":Weight, :Years_Pro, :Position)";
            Query my = conn.createQuery(cmd);
            
            //replace params by value to avoid SQL injection
            my.addParameter("FName", tuple.get("FName"));
            my.addParameter("LName", tuple.get("LName"));
            my.addParameter("Age", new Integer(tuple.get("Age")));
            my.addParameter("Height", new Double(tuple.get("Height")));
            my.addParameter("Weight", new Double(tuple.get("Weight")));
            my.addParameter("Years_Pro", new Integer(tuple.get("Years_Pro")));
            my.addParameter("Position", tuple.get("Position"));
            
            //execute cmd
            my.executeUpdate();
        }
        
        //Fill Teams
        for(HashMap<String, String> tuple : f.getPlayersTuples()){
            String cmd = "INSERT INTO Player Values(:TeamName, :Location, :Conference, :Division, :Wins, :Losses)";
            Query my = conn.createQuery(cmd);
            
            //replace params by value to avoid SQL injection
            my.addParameter("TeamName", tuple.get("TeamName"));
            my.addParameter("Location", tuple.get("Location"));
            my.addParameter("Conference", tuple.get("Conference"));
            my.addParameter("Division", tuple.get("Division"));
            my.addParameter("Winns", new Integer(tuple.get("Winns")));
            my.addParameter("Losses", new Integer(tuple.get("Losses")));
            
            //execute cmd
            my.executeUpdate();
        }
        
        
        //Fill PlaysFor
        for(HashMap<String, String> tuple : f.getPlaysForTuples()){
            String cmd = "INSERT INTO PlaysFor Values(:FName, :LName, :TeamName)";
            Query my = conn.createQuery(cmd);
            
            //replace params by value to avoid SQL injection
            my.addParameter("FName", tuple.get("FName"));
            my.addParameter("LName", tuple.get("LName"));
            my.addParameter("TeamName", tuple.get("TeamName"));
            
            //execute cmd
            my.executeUpdate();
        }
        
        conn.close();
    }
    
    private void fillStatistics(Fetcher f, int newTimeStep){
        for(HashMap<String, String> tuple : f.getStatisticsTuples()){
            //build the cmd
            String cmd = "INSERT INTO Statistics Values(:FName , :LName, :TimeStep, :Games_Played, :Points, :Rebounds,"
                    + " :Assists, :Steals, :Blocks, :Turnovers, :FGM, :ThreePFGM, :FGA, :ThreePFGA, :FTM, :FTA)";
            
            Connection conn = database.open();
            Query my = conn.createQuery(cmd);
            
            //replace params by value (to avoid SQL injection)
            my.addParameter("FName", tuple.get("FName"));
            my.addParameter("LName", tuple.get("LName"));
            my.addParameter("TimeStep", newTimeStep);
            my.addParameter("Games_Played", new Integer(tuple.get("Games_Played")));
            my.addParameter("Points", new Integer(tuple.get("Points")));
            my.addParameter("Rebouds", new Integer(tuple.get("Rebounds")));
            my.addParameter("Assists", new Integer(tuple.get("Assists")));
            my.addParameter("Steals", new Integer(tuple.get("Steals")));
            my.addParameter("Blocks", new Integer(tuple.get("Blocks")));
            my.addParameter("Turnover", new Integer(tuple.get("Turnovers")));
            my.addParameter("FGM", new Integer(tuple.get("FGM")));
            my.addParameter("ThreePFGM", new Integer(tuple.get("ThreePFGM")));
            my.addParameter("FGA", new Integer(tuple.get("FGA")));
            my.addParameter("ThreePFGA", new Integer(tuple.get("ThrePFGA")));
            my.addParameter("FTM", new Integer(tuple.get("FTM")));
            my.addParameter("FTA", new Integer(tuple.get("FTA")));

            //execute the insertion
            my.executeUpdate();
            conn.close(); 
        }
    }
    
    private int getCurrentTimeStep(){
        String cmd = "SELECT max(TimeStep) as u FROM Player";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        if(boh.isEmpty())
            return 0;
        else
            return (Integer) boh.get(0).get("u");
    }
    
    private boolean basicAlreadyFilled(){        
        String cmd = "SELECT * FROM Player";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        return !boh.isEmpty();
    }
    
    /*
     * Prepares the database
     */
    private static Sql2o prepareCuteDatabase(String databaseFileName){        
        //first, write the description of the database
        String[] specs = new String[4];
        specs[0] = "CREATE TABLE IF NOT EXISTS Player(FName TEXT, LName TEXT, Age Integer, Height DECIMAL(5, 2),"
                + " Weight DECIMAL(5, 2), Years_Pro Integer, Position TEXT, PRIMARY KEY (FName, LName))";
        
        specs[1] = "CREATE TABLE IF NOT EXISTS Statistics(FName TEXT, LName TEXT, TimeStep Integer, Games_Played Integer, Points Integer,"
                + " Rebounds Integer, Assists Integer, Steals Integer, Blocks Integer, Turnovers Integer, FGM Integer,"
                + " ThreePFGM Integer, FGA Integer, ThreePFGA Integer, FTM Integer, FTA Integer, PRIMARY KEY(FName, LName, TimeStep))";
        
        specs[2] = "CREATE TABLE IF NOT EXISTS Team(TeamName TEXT, Location TEXT, Conference TEXT, Division TEXT, Wins Integer,"
                + " Losses Integer, PRIMARY KEY(TeamName))";
        
        specs[3] = "CREATE TABLE IF NOT EXISTS PlaysFor(FName TEXT, LName TEXT, TeamName TEXT, PRIMARY KEY(FName, LName, TeamName))";
        
        //check that data folder exists:
        File directory = new File(DIRECTORY_NAME);
        if(!directory.exists())
            directory.mkdir();
        
        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + DIRECTORY_NAME + File.separator + FILE_NAME);
        
        //creating the object to return
        Sql2o toReturn = new Sql2o(source);

        //run all commands
        Connection conn = toReturn.open();
        for(String cmd: specs)
            conn.createQuery(cmd).executeUpdate();
        conn.close();
        
        return toReturn;
    }
    
    public enum PlayerAttr{FName, LName, Age, Height, Weight, Years_Pro, Position}
    public enum StatisticsAttr{FName, LName, Games_Played, Points, Rebouds, Assists, Steals, Blocks, Turnovers, FGM, TreePFGM, FGA, TreePFGA, FTM, FTA}
    public enum PlaysForAttr{FName, LName, TeamName}
    public enum TeamAttr{Location, TeamName, Conference, Division, Wins, Losses}
     
}
