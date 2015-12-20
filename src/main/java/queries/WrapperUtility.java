/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 20, 2015
 */

package queries;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

import datastoring.SQLStorer;
import queries.Graph.Value;

public class WrapperUtility {

    private final Sql2o database;

    public WrapperUtility(){
        this.database = prepareCuteDatabase("");
    }

    /*
     * Prepares the database
     */
    private static Sql2o prepareCuteDatabase(String databaseFileName){        
        //check that data folder exists:
        File directory = new File(SQLStorer.DIRECTORY_NAME);
        if(!directory.exists())
            directory.mkdir();

        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + SQLStorer.DIRECTORY_NAME + File.separator + SQLStorer.FILE_NAME);

        //creating the database and returning.
        return new Sql2o(source);
    }

    public String[] getNames(){
        String cmd = "SELECT P.FName, P.LName FROM Player as P";

        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();

        String[] toReturn = new String[boh.size()];
        for(int i = 0; i < boh.size(); i++){
            String FName = (String) boh.get(i).get("fname");
            String LName = (String) boh.get(i).get("lname");
            
            toReturn[i] = FName + " " + LName;
        }

        return toReturn;
    }

    public String[] getAttributes(){
        String[] toReturn = new String[13];

        toReturn[0] = "games_Played";
        toReturn[1] = "points";
        toReturn[2] = "rebounds";
        toReturn[3] = "assists";
        toReturn[4] = "steals";
        toReturn[5] = "blocks";
        toReturn[6] = "turnovers";
        toReturn[7] = "FGM";
        toReturn[8] = "ThreePFGM";
        toReturn[9] = "FGA";
        toReturn[10] = "ThreePFGA";
        toReturn[11] = "FTM";
        toReturn[12] = "FTA";
        
        return toReturn;
    }

    public String[] getComputations(){
        String[] toReturn = new String[2];

        toReturn[0] = "absolute";
        toReturn[1] = "cumulative";

        return toReturn;
    }

    /**
     * Gives the values 
     * 
     * @param method The method absolute or cumulative
     * @param attribute The name of the attribute
     * @param playerName The name of the palyer "John Doe"
     * @return
     */
    public List<Value> getValues(String method, String attribute, String playerName){
        String FName = playerName.split(" ")[0];
        String LName = playerName.split(" ")[1];

        String cmd = "SELECT D.Date, S."+attribute+" FROM Statistics as S, Date as D where S.FName = :FName AND S.LName = :LName and D.TimeStep = S.TimeStep";

        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("FName", FName).
                addParameter("LName", LName).
                executeAndFetchTable().asList();
        conn.close();

        ArrayList<Value> toReturn = new ArrayList<>();
        for(Map<String, Object> m : boh){
            String date = (String) m.get("date");
            Integer value = (Integer) m.get(attribute.toLowerCase());
            toReturn.add(new Value(date, value));
        }

        if(method.equals("cumulative"))
            return toReturn;

        int sum = 0;
        for(Value v : toReturn){
            v.val = v.val - sum;
            sum += v.val;
        }

        return toReturn;
    }

    /**
     * Gives the max value 
     * 
     * @param method The method absolute or cumulative
     * @param attribute The name of the attribute
     * @param playerName The name of the palyer "John Doe"
     * @return
     */
    public int getMaxValue(String method, String attribute){                
        if(method.equals("absolute"))
            return 5;
        else{
            String cmd = "SELECT max(S."+attribute+") as m FROM Statistics as S";

            Connection conn = database.open();
            List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
            conn.close();

            return (Integer) boh.get(0).get("m");
        }

    }

}
