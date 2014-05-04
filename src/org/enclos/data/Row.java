package org.enclos.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Row implements Comparable {
    private int score = 0;
    private final List<String> values;
    
    /**
     * Row constructor, row in table score
     * @param player
     */
    public Row(Human player) {
        this.score = player.getNumberOfGamesWon();

        values = new ArrayList<>();
        values.add(player.getFirstName());
        values.add(player.getLastName());
        values.add(String.valueOf(player.getAge()));
        values.add(String.valueOf(player.getNumberOfGamesWon()));
        values.add(String.valueOf(player.getNumberOfGamesLost()));
    }


    /**
     * Implement comparable interface method
     */
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if (o instanceof Row) {
            Row rowToCompare = (Row) o;

            if (this.score > rowToCompare.getScore()) {
                return -1;
            }
            if (this.score < rowToCompare.getScore()) {
                return 1;
            } else
                return 0;

        } else
            return 1;
    }
    
    /**
     * Getter score attribute
     * @return int score
     */
    public int getScore() {
        return this.score;
    }
    
    /**
     * Getter values attribute
     * @return List<String> the values of the row
     */
    public List<String> getValues() {
        return this.values;
    }

}