package org.enclos.ui;

import java.awt.Dimension;
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

import org.enclos.data.Human;
import org.enclos.data.Row;

public class HelpPanel extends JPanel {

	public HelpPanel() {
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Help");
		setBorder(title);
		feedTable();
	}

	public void feedTable() {
		String[] columns = { "Shortcut", "Description" };
		Object[][] data = { { "CTRL+Q", "Exit" }, { "CTRL+N", "Create a new game" }, { "CTRL+S", "Save displayed game" }, { "S", "Show scores" }, { "G", "Show current game" }, { "P", "Show player manager" }, { "F1", "Help" }, { "F11", "Fullscreen mode" },{ "← →", "Switch game" } };

		JTable table = new JTable(data,columns) {

		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		table.setFocusable(false);
		table.setPreferredSize(new Dimension(500, 143));

		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);

		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		add(new JScrollPane(table));
	}

}
