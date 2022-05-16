import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkListPanel extends JPanel{
	
	private String[] headers = {"", "Group", "Name", "URL", "Created Time", "Memo"};
	
	private DefaultTableModel model; 
	private JTable table;
	private JScrollPane scrollPane;
	
	BookmarkListPanel(BookmarkList bl){
		setLayout(new GridLayout());
		
		// Jtable, model
		model = new DefaultTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);

		for (int i = 0; i<bl.numBookmarks(); i++) {
			addBookmark(bl.getBookmark(i));
		}
		
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMinWidth(10);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(160);
		
		// scroll pane
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(650, 300));
		
		add(scrollPane);
		setVisible(true);
	}
	
	// bookmark ���� �ϳ��� add �ϴ� �޼ҵ�. �ʱ� ���ÿ��� ���Ǹ�, ���Ŀ� 1���� add�Ҷ��� ��밡��
	private void addBookmark(Bookmark bm) {
		String groupName = bm.getGroup();
		
		if (groupName.equals("")) {
			// �׷� ����, �׳� �߰�
			model.addRow(new String[]{"", "", bm.getName(), bm.getUrl(), bm.getTime(), bm.getMemo()});
		}
		else {
			// �׷� ����, ������ �߰������� �ִ� �׷����� Ȯ���ϱ�
			boolean hasGroupName = false;
			for (int i = 0; i<model.getRowCount(); i++) {
				if (model.getValueAt(i, 1).equals(groupName)) {
					hasGroupName = true;
					break;
				}
			}
			
			if (hasGroupName == true) {
				// �� �׷� �̸��� ����, �ƹ��͵� ����. 
				// ���� 6������ �ٸ� ó���� �������� ����.
			}
			else {
				// �� �׷� �̸��� ����, ���� ���ο� �׷� �߰�
				model.addRow(new String[]{">", groupName, "", "", "", ""});
			}
		}
		
		// table���� Ȯ���� ���̰�
		model.fireTableDataChanged();
	}

}
