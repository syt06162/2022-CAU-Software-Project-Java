import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkAddFrame extends JFrame{
	JButton inputBtn;
	
	BookmarkAddFrame(){
		setLayout(new BorderLayout());
		setTitle("Input New Bookmark");
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// centerø° input panel, eastø° input btn
		BookmarkInputPanel inputPanel = new BookmarkInputPanel();
		add(inputPanel, BorderLayout.CENTER);
		
		inputBtn = new JButton("Input");
		add(inputBtn, BorderLayout.EAST);
		
	
		setVisible(true);
	}
}

class BookmarkInputPanel extends JPanel{
	private String[] headers = {"Group", "Name", "URL", "Memo"};
	private DefaultTableModel model; 
	private JTable table;
	private JScrollPane scrollPane;
	
	BookmarkInputPanel(){
		setLayout(new GridLayout());
		// Jtable, model
		model = new DefaultTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);
		
		// ∫Û¡Ÿ 1∞≥ add
		model.addRow(new String[]{"","","",""});
		model.fireTableDataChanged();
		
		table = new JTable(model);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
				
		// scroll pane
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(650, 300));
		
		add(scrollPane);
		setVisible(true);
	}
	
}
