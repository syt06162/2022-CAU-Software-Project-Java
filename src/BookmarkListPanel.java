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
	
	// bookmark 정보 하나를 add 하는 메소드. 초기 세팅에도 사용되며, 추후에 1개씩 add할때도 사용가능
	private void addBookmark(Bookmark bm) {
		String groupName = bm.getGroup();
		
		if (groupName.equals("")) {
			// 그룹 없음, 그냥 추가
			model.addRow(new String[]{"", "", bm.getName(), bm.getUrl(), bm.getTime(), bm.getMemo()});
		}
		else {
			// 그룹 있음, 기존에 추가된적이 있던 그룹인지 확인하기
			boolean hasGroupName = false;
			for (int i = 0; i<model.getRowCount(); i++) {
				if (model.getValueAt(i, 1).equals(groupName)) {
					hasGroupName = true;
					break;
				}
			}
			
			if (hasGroupName == true) {
				// 이 그룹 이름이 있음, 아무것도 안함. 
				// 과제 6에서는 다른 처리가 있을수도 있음.
			}
			else {
				// 이 그룹 이름이 없음, 따라서 새로운 그룹 추가
				model.addRow(new String[]{">", groupName, "", "", "", ""});
			}
		}
		
		// table에도 확실히 보이게
		model.fireTableDataChanged();
	}

}
