/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 18, 2015
 */

package datafetching;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DemoFetcher extends Fetcher {

    private final Random rand = new Random(1995);
    
    private String date;

    private String[][] p = new String[434][7];

    //private String[][] p = {{"Jose",    "Calderon", "34", "75.34", "120", "10", "ATK"},
    //                        {"Joel",    "Embid",    "25", "79.21", "150", "2", "DEF"},
    //                        {"Anthony", "Carmelo",  "30", "78.12", "160", "4","MID"}};

    private String[][] t = {{"Toronto", "Raptors", "Eastern", "Atlantic", "17", "12"},
                            {"Boston", "Celtics", "Eastern", "Atlantic", "14",  "13"},
                            {"New York","Knicks", "Eastern", "Atlantic", "14", "14"},
            {"Brooklyn", "Nets", "Eastern", "Atlantic", "7",  "20"},
            {"Philadelphia", "Sixers", "Eastern", "Atlantic", "1",  "28"},
            {"Cleveland", "Cavaliers", "Eastern", "Central", "18",  "7"},
            {"Indiana", "Pacers", "Eastern", "Central", "16",  "10"},
            {"Chicago", "Bulls", "Eastern", "Central", "15",  "10"},
            {"Detroit", "Pistons", "Eastern", "Central", "16",  "12"},
            {"Milwaukee", "Bucks", "Eastern", "Central", "11",  "18"},
            {"Miami", "Heat", "Eastern", "Southeast", "16",  "10"},
            {"Atlanta", "Hawks", "Eastern", "Southeast", "17",  "12"},
            {"Charlotte", "Hornets", "Eastern", "Southeast", "15",  "11"},
            {"Orlando", "Magic", "Eastern", "Southeast", "15",  "12"},
            {"Washington", "Wizards", "Eastern", "Southeast", "11",  "14"},
            {"Oklahoma City", "Thunder", "Western", "Northwest", "18",  "9"},
            {"Utah", "Jazz", "Western", "Northwest", "11",  "14"},
            {"Denver", "Nuggets", "Western", "Northwest", "11",  "16"},
            {"Minnesota", "Timberwolves", "Western", "Northwest", "11",  "16"},
            {"Portland", "Trailblazers", "Western", "Northwest", "11",  "18"},
            {"Golden State", "Warriors", "Western", "Pacific", "26",  "1"},
            {"LA", "Clippers", "Western", "Pacific", "16",  "12"},
            {"Phoenix", "Suns", "Western", "Pacific", "12",  "17"},
            {"Sacramento", "Kings", "Western", "Pacific", "11",  "16"},
            {"LA", "Lakers", "Western", "Pacific", "4",  "23"},
            {"San Antonio", "Spurs", "Western", "Southwest", "23",  "5"},
            {"Dallas", "Mavericks", "Western", "Southwest", "15",  "12"},
            {"Memphis", "Grizzlies", "Western", "Southwest", "15",  "14"},
            {"Houston", "Rockets", "Western", "Southwest", "14",  "14"},
            {"New Orleans", "Hornets", "Western", "Southwest", "8",  "19"}};

    private Map<String, String> teamNames = new HashMap();
    
    //private String[][] pfor = {{"Jose",    "Calderon", "Knicks"},
     //                          {"Joel",    "Embid",    "Sixers"},
      //                         {"Anthony", "Carmelo", " Knicks"}};

    private String[][] pfor = new String[434][3];

    private String[][] pstat = new String[434][15];

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

        populateTeamNames();


        
        this.iterations = iterations;
    }

    private void populateTeamNames() {
        teamNames.put("GS", "Warriors");
        teamNames.put("HOU", "Rockets");
        teamNames.put("OKC", "Thunder");
        teamNames.put("CLE", "Cavaliers");
        teamNames.put("IND", "Pacers");
        teamNames.put("SAC", "Kings");
        teamNames.put("POR", "Trailblazers");
        teamNames.put("LAC", "Clippers");
        teamNames.put("NO", "Pelicans");
        teamNames.put("TOR", "Raptors");
        teamNames.put("PHO", "Suns");
        teamNames.put("NY", "Knicks");
        teamNames.put("BOS", "Celtics");
        teamNames.put("CHI", "Bulls");
        teamNames.put("MIN", "Timberwolves");
        teamNames.put("SA", "Spurs");
        teamNames.put("DET", "Pistons");
        teamNames.put("WAS", "Wizards");
        teamNames.put("BKN", "Nets");
        teamNames.put("UTA", "Jazz");
        teamNames.put("MIA", "Heat");
        teamNames.put("ATL", "Hawks");
        teamNames.put("CHA", "Hornets");
        teamNames.put("PHI", "Sixers");
        teamNames.put("DEN", "Nuggets");
        teamNames.put("DAL", "Mavericks");
        teamNames.put("LAL", "Lakers");
        teamNames.put("MIL", "Bucks");
        teamNames.put("MEM", "Grizzlies");
        teamNames.put("ORL", "Magic");

    }

    private void scrapeStats(){

        //{Fname, Lname, GP, Points, Rebounds, Assists, Steals, Blocks, Turnovers, FGM, ThreePFGM, FGA, ThreePFGA, FTM, FTA}
        try{
            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            userAgent.visit("https://sports.yahoo.com/nba/stats/byposition?pos=PG%2CSG%2CG%2CGF%2CSF%2CPF%2CF%2CFC%2CC&sort=25&qualified=0&conference=NBA&year=season_2015");

            //Get all the players
            Elements players1 = userAgent.doc.findEach("<tr class=\"ysprow1\">");
            Elements players2 = userAgent.doc.findEach("<tr class=\"ysprow2\">");
            int index = 0;
            //Get stats for each player and add them to data structure
            for(Element player : players1) {
                Elements stats = player.findEach("<td class=\"yspscores\">");

                //Fname and Lname
                String name = stats.getElement(0).findFirst("<a>").getText();
                String fname = name.substring(0, name.indexOf(" "));
                String lname = name.substring(name.indexOf(" ") + 1, name.length());

                pstat[index][0] = fname;
                pstat[index][1] = lname;

                //Populating pfor
                pfor[index][0] = fname;
                pfor[index][1] = lname;
                String teamIndex = stats.getElement(1).findFirst("<a>").getText();
                pfor[index][2] = this.teamNames.get(teamIndex);

                //GP
                String rawgp = stats.getElement(2).getText();
                String gp = rawgp.substring(0, rawgp.indexOf("&"));

                pstat[index][2] = gp;

                //Rebounds
                String rawrebs = stats.getElement(18).getText();
                String rebs = String.valueOf((int) Double.parseDouble(rawrebs.substring(0, rawrebs.indexOf("&"))));

                pstat[index][4] = rebs;
                //Assists
                String rawassists = stats.getElement(20).getText();
                String assists = String.valueOf((int) Double.parseDouble(rawassists.substring(0, rawassists.indexOf("&"))));

                pstat[index][5] = assists;
                //Steals
                String rawstls = stats.getElement(22).getText();
                String stls = String.valueOf((int) Double.parseDouble(rawstls.substring(0, rawstls.indexOf("&"))));

                pstat[index][6] = stls;
                //Blocks
                String rawblks = stats.getElement(23).getText();
                String blks = String.valueOf((int) Double.parseDouble(rawblks.substring(0, rawblks.indexOf("&"))));


                pstat[index][7] = blks;
                //TO
                String rawtos = stats.getElement(21).getText();
                String tos = String.valueOf((int) Double.parseDouble(rawtos.substring(0, rawtos.indexOf("&"))));
                pstat[index][8] = tos;

                //FGM
                String rawfgm = stats.getElement(4).getText();
                String fgm = String.valueOf((int) Double.parseDouble(rawfgm.substring(0, rawfgm.indexOf("&"))));


                pstat[index][9] = fgm;

                //3PFGM
                String rawthreem = stats.getElement(8).getText();
                String threem = String.valueOf((int) Double.parseDouble(rawthreem.substring(0, rawthreem.indexOf("&"))));

                pstat[index][10] = threem;
                //FGA
                String rawfga = stats.getElement(5).getText();
                String fga = String.valueOf((int) Double.parseDouble(rawfga.substring(0, rawfga.indexOf("&"))));

                pstat[index][11] = fga;

                //3PFGA
                String rawthreea = stats.getElement(9).getText();
                String threea = String.valueOf((int) Double.parseDouble(rawthreea.substring(0, rawthreea.indexOf("&"))));

                pstat[index][12] = threea;

                //FTM
                String rawftm = stats.getElement(12).getText();
                String ftm = String.valueOf((int) Double.parseDouble(rawftm.substring(0, rawftm.indexOf("&"))));

                pstat[index][13] = ftm;
                //FTA
                String rawfta = stats.getElement(13).getText();
                String fta = String.valueOf((int) Double.parseDouble(rawfta.substring(0, rawfta.indexOf("&"))));

                pstat[index][14] = fta;
                //Points
                String points = player.findFirst("<td class=\"ysptblclbg6\">").findFirst("<span>").getText();
                String pts = String.valueOf((int)Double.parseDouble(points));
                pstat[index][3] = pts;

                index++;
            }

            for(Element player : players2) {
                Elements stats = player.findEach("<td class=\"yspscores\">");

                //Fname and Lname
                String name = stats.getElement(0).findFirst("<a>").getText();
                String fname = name.substring(0, name.indexOf(" "));
                String lname = name.substring(name.indexOf(" ") + 1, name.length());

                pstat[index][0] = fname;
                pstat[index][1] = lname;

                //Populating pfor
                pfor[index][0] = fname;
                pfor[index][1] = lname;
                String teamIndex = stats.getElement(1).findFirst("<a>").getText();
                pfor[index][2] = this.teamNames.get(teamIndex);

                //GP
                String rawgp = stats.getElement(2).getText();
                String gp = rawgp.substring(0, rawgp.indexOf("&"));

                pstat[index][2] = gp;

                //Rebounds
                String rawrebs = stats.getElement(18).getText();
                String rebs = String.valueOf((int) Double.parseDouble(rawrebs.substring(0, rawrebs.indexOf("&"))));

                pstat[index][4] = rebs;
                //Assists
                String rawassists = stats.getElement(20).getText();
                String assists = String.valueOf((int) Double.parseDouble(rawassists.substring(0, rawassists.indexOf("&"))));

                pstat[index][5] = assists;
                //Steals
                String rawstls = stats.getElement(22).getText();
                String stls = String.valueOf((int) Double.parseDouble(rawstls.substring(0, rawstls.indexOf("&"))));

                pstat[index][6] = stls;
                //Blocks
                String rawblks = stats.getElement(23).getText();
                String blks = String.valueOf((int) Double.parseDouble(rawblks.substring(0, rawblks.indexOf("&"))));


                pstat[index][7] = blks;
                //TO
                String rawtos = stats.getElement(21).getText();
                String tos = String.valueOf((int) Double.parseDouble(rawtos.substring(0, rawtos.indexOf("&"))));
                pstat[index][8] = tos;

                //FGM
                String rawfgm = stats.getElement(4).getText();
                String fgm = String.valueOf((int) Double.parseDouble(rawfgm.substring(0, rawfgm.indexOf("&"))));


                pstat[index][9] = fgm;

                //3PFGM
                String rawthreem = stats.getElement(8).getText();
                String threem = String.valueOf((int) Double.parseDouble(rawthreem.substring(0, rawthreem.indexOf("&"))));

                pstat[index][10] = threem;
                //FGA
                String rawfga = stats.getElement(5).getText();
                String fga = String.valueOf((int) Double.parseDouble(rawfga.substring(0, rawfga.indexOf("&"))));

                pstat[index][11] = fga;

                //3PFGA
                String rawthreea = stats.getElement(9).getText();
                String threea = String.valueOf((int) Double.parseDouble(rawthreea.substring(0, rawthreea.indexOf("&"))));

                pstat[index][12] = threea;

                //FTM
                String rawftm = stats.getElement(12).getText();
                String ftm = String.valueOf((int) Double.parseDouble(rawftm.substring(0, rawftm.indexOf("&"))));

                pstat[index][13] = ftm;
                //FTA
                String rawfta = stats.getElement(13).getText();
                String fta = String.valueOf((int) Double.parseDouble(rawfta.substring(0, rawfta.indexOf("&"))));

                pstat[index][14] = fta;
                //Points
                String points = player.findFirst("<td class=\"ysptblclbg6\">").findFirst("<span>").getText();
                String pts = String.valueOf((int)Double.parseDouble(points));
                pstat[index][3] = pts;

                index++;
            }


            //Elements elements = userAgent.doc.findFirst("<tr class=\"ysprow1\">").findEach("<td class=\"yspscores\">");
            //System.out.println("Player search: " + elements.size() + " results");//report number of search results.
            //String rawmins = elements.getElement(2).getText();
            //String name = elements.getElement(0).findFirst("<a>").getText();
            //System.out.println(name + "'s minutes per game: " + rawmins.substring(0, rawmins.indexOf("&")));
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }
    }

    private void scrapePlayerInfo() {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit("https://sports.yahoo.com/nba/stats/byposition?pos=PG%2CSG%2CG%2CGF%2CSF%2CPF%2CF%2CFC%2CC&sort=25&qualified=0&conference=NBA&year=season_2015");//create new userAgent (headless browser).


            //Get all the players
            Elements players1 = userAgent.doc.findEach("<tr class=\"ysprow1\">");
            Elements players2 = userAgent.doc.findEach("<tr class=\"ysprow2\">");
            int index = 0;

            for(Element player : players1) {
                String playerURL = player.findFirst("<td class=\"yspscores\">").findFirst("<a href>").getAt("href");
                userAgent.visit(playerURL);

                Element pinfo = userAgent.doc.findFirst("<div class=\"player-info\">");
                Element tinfo = userAgent.doc.findFirst("<span class=\"team-info\">");

                //Fname and Lname
                String name = pinfo.findFirst("<h1>").getText();
                String fname = name.substring(0, name.indexOf(" "));
                String lname = name.substring(name.indexOf(" ") + 1, name.length());
                System.out.println(fname);
                p[index][0] = fname;
                p[index][1] = lname;

                //Age
                Element infoList = userAgent.doc.findFirst("<div class=\"bio\">");
                Elements bioElements = infoList.findEach("<dd>");



                String rawage = bioElements.getElement(2).getText();
                String age = rawage.substring(rawage.indexOf(", ") + 2, rawage.length());
                age = String.valueOf(2016 - Integer.parseInt(age));
                System.out.println(age);

                p[index][2] = age;
                //Height
                String rawheight = bioElements.getElement(0).getText();
                int feetInInches = Integer.parseInt(rawheight.substring(0, rawheight.indexOf("-"))) * 12;
                int inches = Integer.parseInt(rawheight.substring(rawheight.indexOf("-") + 1, rawheight.length()));
                String ht = String.valueOf(feetInInches + inches);
                System.out.println(ht);

                p[index][3] = ht;
                //Weight

                String weight = bioElements.getElement(1).getText();
                System.out.println(weight);

                p[index][4] = weight;
                //Years Pro
                String rawyears = bioElements.getElement(5).getText();
                int j = rawyears.indexOf(" ");
                String years = "";
                if (j == -1) {
                    years = "1";
                } else {
                    years = String.valueOf(2016 - Integer.parseInt(rawyears.substring(0, rawyears.indexOf(" "))));
                }

                p[index][5] = years;
                //Position
                String rawpos = tinfo.getText();
                String pos = "";
                if (rawpos.charAt(rawpos.indexOf(", ") + 3) == ',') {
                    pos = rawpos.substring(rawpos.indexOf(", ") + 2, rawpos.indexOf(", ") + 3);
                } else {
                    pos = rawpos.substring(rawpos.indexOf(", ") + 2, rawpos.indexOf(", ") + 4);
                }
                p[index][6] = pos;
                System.out.println(pos);

                index++;
            }

            for(Element player : players2) {
                String playerURL = player.findFirst("<td class=\"yspscores\">").findFirst("<a href>").getAt("href");
                userAgent.visit(playerURL);

                Element pinfo = userAgent.doc.findFirst("<div class=\"player-info\">");
                Element tinfo = userAgent.doc.findFirst("<span class=\"team-info\">");

                //Fname and Lname
                String name = pinfo.findFirst("<h1>").getText();
                String fname = name.substring(0, name.indexOf(" "));
                String lname = name.substring(name.indexOf(" ") + 1, name.length());
                p[index][0] = fname;
                p[index][1] = lname;

                //Age
                Element infoList = userAgent.doc.findFirst("<ul id=\"yui_3_18_1_1_1450683096989_743\">");
                Elements bioElements = infoList.findEach("<dd>");



                String rawage = bioElements.getElement(2).getText();
                String age = rawage.substring(rawage.indexOf(", ") + 2, rawage.length());
                age = String.valueOf(2016 - Integer.parseInt(age));

                p[index][2] = age;
                //Height
                String rawheight = bioElements.getElement(0).getText();
                int feetInInches = Integer.parseInt(rawheight.substring(0, rawheight.indexOf("-"))) * 12;
                int inches = Integer.parseInt(rawheight.substring(rawheight.indexOf("-") + 1, rawheight.length()));
                String ht = String.valueOf(feetInInches + inches);

                p[index][3] = ht;
                //Weight

                String weight = bioElements.getElement(1).getText();

                p[index][4] = weight;
                //Years Pro
                String rawyears = bioElements.getElement(5).getText();
                int j = rawyears.indexOf(" ");
                String years = "";
                if (j == -1) {
                    years = "1";
                } else {
                    years = String.valueOf(2016 - Integer.parseInt(rawyears.substring(0, rawyears.indexOf(" "))));
                }

                p[index][5] = years;
                //Position
                String rawpos = tinfo.getText();
                String pos = "";
                if (rawpos.charAt(rawpos.indexOf(", ") + 3) == ',') {
                    pos = rawpos.substring(rawpos.indexOf(", ") + 2, rawpos.indexOf(", ") + 3);
                } else {
                    pos = rawpos.substring(rawpos.indexOf(", ") + 2, rawpos.indexOf(", ") + 4);
                }
                p[index][6] = pos;

                index++;
            }





        } catch(JauntException e) {
            System.err.println(e);
        }
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
        for(int i = 0; i < pstat.length; i++){
            HashMap<String, String> toPut = new HashMap<>();
            toPut.put("FName", pstat[i][0]);
            toPut.put("LName", pstat[i][1]);
            toPut.put("Games_Played", pstat[i][2]);
            toPut.put("Points", pstat[i][3]);
            toPut.put("Rebounds",pstat[i][4]);
            toPut.put("Assists", pstat[i][5]);
            toPut.put("Steals", pstat[i][6]);
            toPut.put("Blocks", pstat[i][7]);
            toPut.put("Turnovers", pstat[i][8]);
            toPut.put("FGM", pstat[i][9]);
            toPut.put("ThreePFGM", pstat[i][10]);
            toPut.put("FGA", pstat[i][11]);
            toPut.put("ThreePFGA",pstat[i][12]);
            toPut.put("FTM", pstat[i][13]);
            toPut.put("FTA", pstat[i][14]);
            
            statsTuples.add(toPut);
        }
        
    }

    private void addToStats(int it){
        for(int i = 0; i < p.length; i++){
            statsTuples.get(i).put("Games_Played", ""+ (new Integer(statsTuples.get(i).get("Games_Played")) + rand.nextInt(4)));
            statsTuples.get(i).put("Points", ""+ (new Integer(statsTuples.get(i).get("Points")) + rand.nextInt(4)));
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
        
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        @SuppressWarnings("deprecation")
        Date date = new Date(2014, 12, 5 + it);        
        this.date = dateFormat.format(date);
    }
    
    @Override
    public void startProcess() {
        scrapeStats();
        scrapePlayerInfo();
        fillBasicTuples();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        this.date = dateFormat.format(date);
        super.notifyDataWasFetched();
        /*
        for(int i = 0; i < iterations; i++){
            simulateWait();
            //addToStats(i);
            simulateWait();
            super.notifyDataWasFetched();
        }*/
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
        //{Fname, Lname, GP, Points, Rebounds, Assists, Steals, Blocks, Turnovers, FGM, ThreePFGM, FGA, ThreePFGA, FTM, FTA}
        /*try{
            UserAgent userAgent = new UserAgent();
            userAgent.visit("https://sports.yahoo.com/nba/stats/byposition?pos=PG%2CSG%2CG%2CGF%2CSF%2CPF%2CF%2CFC%2CC&sort=25&qualified=0&conference=NBA&year=season_2015");//create new userAgent (headless browser).
            //userAgent.visit("https://sports.yahoo.com/nba/players/4612/");
            // visit a url
            //Elements stats = player.findEach("<td class=\"yspscores\">")
            Element steph = userAgent.doc.findFirst("<tr class=\"ysprow1\">").findFirst("<td class=\"yspscores\">");
            String stephURL = steph.findFirst("<a href>").getAt("href");
            userAgent.visit(stephURL);
            Element player = userAgent.doc.findFirst("<div class=\"player-info\">");
            Element team = userAgent.doc.findFirst("<span class=\"team-info\">");
            System.out.println("Name " + player.findFirst("<h1>").getText());
            String rawpos = team.getText();
            System.out.println("Position " + rawpos.substring(rawpos.indexOf(", ") + 2, rawpos.indexOf(", ") + 3));
            //Elements players1 = userAgent.doc.findEach("<tr class=\"ysprow1\">");
            //Elements players2 = userAgent.doc.findEach("<tr class=\"ysprow2\">");

            //Elements elements = userAgent.doc.findFirst("<tr class=\"ysprow1\">").findEach("<td class=\"yspscores\">");
             //System.out.println("Player search: " + (players1.size() + players2.size()) + " results");//report number of search results.
            //String rawmins = elements.getElement(2).getText();
           // String name = elements.getElement(0).findFirst("<a>").getText();
           // System.out.println(name + "'s minutes per game: " + rawmins.substring(0, rawmins.indexOf("&")));
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }*/
        try {
            System.out.print("Data fetching...");
            Thread.sleep(500);
            System.out.println("... done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDate() {
        return date;
    }


}
