package org.enclos.data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class SimpleReader {

    private String jsonFilePath;
    private Object file;
    
    /**
     * Constructor of the SimpleReader class
     * @param fileName
     */
    public SimpleReader(String fileName) {
        JSONParser parser = new JSONParser();
        try {
            this.file = parser.parse(new FileReader("resources/save/" + fileName + ".json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the json file given as argument 
     * @param fileName
     * @return Board's data
     */
    public static Map<String, Object> readGame(String fileName) {
        JSONParser parser = new JSONParser();
        Object targetFile = null;
        try {
            targetFile = parser.parse(new FileReader("resources/save/" + fileName + ".json"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> values = new HashMap<String, Object>();
        try {
            JSONObject reader = (JSONObject) targetFile;

            for (Params param : Params.values()) {
                Object value = reader.get(param.toString());
                values.put(param.toString(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    
    /**
     * Read the json file given as argument
     * @param fileName
     * @return Players' data 
     */
    public static List<Human> readPlayer(String fileName) {
        JSONParser parser = new JSONParser();
        Object targetFile = null;
        try {
            targetFile = parser.parse(new FileReader("resources/players/" + fileName + ".json"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Human> listPlayers = new ArrayList<>();
        try {
            JSONObject reader = (JSONObject) targetFile;
            JSONArray players = (JSONArray) reader.get("Players");

            Iterator iterator = players.iterator();
            while (iterator.hasNext()) {
                JSONArray currentPlayer = (JSONArray) iterator.next();
                String[] playerInfo = ((String) currentPlayer.get(0)).split(",");

                String lastName = playerInfo[0];
                String firstName = playerInfo[1];
                int age = Integer.parseInt(playerInfo[2]);
                int gamesWon =  Integer.parseInt(playerInfo[3]);
                int gamesLost =  Integer.parseInt(playerInfo[4]);
                String picturePath = playerInfo[5];

                listPlayers.add(new Human(firstName, lastName, age,gamesWon, gamesLost, picturePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listPlayers;
    }
    
    /**
     * Enum of informations pick in different json file
     * @author Clement
     *
     */
    private enum Params {
        SHEEPNUMBER, BOARDSIZE, PLAYERS, SHEEPSPOSITIONS, BARRIERS; 

        @Override
        public String toString() {
            // only capitalize the first letter
            String s = super.toString();
            return s.substring(0, 1) + s.substring(1).toLowerCase();
        }
    }
}