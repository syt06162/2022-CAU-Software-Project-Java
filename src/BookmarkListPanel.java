import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkListPanel extends JPanel{
	
	private String[] headers = {"", "Group", "Name", "URL", "Created Time", "Memo"};
	
	BookmarkListPanel(){
		setLayout(new GridLayout());
		
		// table
		DefaultTableModel model; 
		JTable table;
		JScrollPane scrollPane;
		
		model = new DefaultTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);
		
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(650, 300));
		
		add(scrollPane);
//		value = model.getValueAt(row, col);
//		model.setValueAt(value, row, col);
//		model.fireTableDataChanged();
//		index = table.getSelectedRow();
		
		setVisible(true);
	}

}
