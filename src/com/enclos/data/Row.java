package com.enclos.data;

import java.util.ArrayList;
import java.util.List;

public class Row implements Comparable {
    private int score = 0;
    private final List<String> values;

    public Row(Player player) {
        this.score = player.getNumberOfGamesWon();

        values = new ArrayList<>();
        values.add(player.getFirstName());
        values.add(player.getLastName());
        values.add(String.valueOf(player.getAge()));
        values.add(String.valueOf(player.getNumberOfGamesWon()));
        values.add(String.valueOf(player.getNumberOfGamesLost()));
    }

    // reverse order
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

    public int getScore() {
        return this.score;
    }

    public List<String> getValues() {
        return this.values;
    }

}