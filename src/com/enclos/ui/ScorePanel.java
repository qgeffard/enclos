package com.enclos.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.enclos.data.Human;
import com.enclos.data.Row;

public class ScorePanel extends JPanel {

	public ScorePanel(List<Human> players) {
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Score");
		setBorder(title);
		
		feedTable(players);
	}

	public void feedTable(List<Human> players) {
		this.removeAll();
		List<String> columns = Arrays.asList("First Name", "Last Name", "Age", "Games won", "Games lost");

		List<Row> rows = new ArrayList<Row>();

		for (Human player : players) {
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
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		add(new JScrollPane(table));
	}

}