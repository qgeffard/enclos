package com.enclos.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.enclos.data.Player;
import com.enclos.data.Row;

public class ScorePanel extends JPanel {

    public ScorePanel(List<Player> players) {
        feedTable(players);
    }

    public void feedTable(List<Player> players) {
        this.removeAll();
        List<String> columns = Arrays.asList("First Name", "Last Name", "Age", "Score");

        List<Row> rows = new ArrayList<Row>();

        for (Player player : players) {
            rows.add(new Row(player));
        }

        Collections.sort(rows);
        String[][] array = new String[rows.size()][];
        String[] blankArray = new String[0];
        for (int i = 0; i < rows.size(); i++) {
            array[i] = rows.get(i).getValues().toArray(blankArray);
        }

        final JTable table = new JTable(array, columns.toArray()) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        add(new JScrollPane(table));
    }

}