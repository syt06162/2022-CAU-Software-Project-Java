import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkListPanel extends JPanel{
	
	// BookmarkManager ���� �����ǰ� �ִ� ���� �������� BookmarkList �� ���� ȭ��ǥ
	// �̸� �˰� �����Ƿ�, showBookmarks �� ���� ��� bookmark���� ���������� �ҷ��� �� �ִ�.
	private BookmarkList bl;
	
	// JTable����, ���� open������ �׷� �̸����� �����ϴ� ����Ʈ.
	// showBookmarks �޼ҵ� ���� ��, �� ����Ʈ�� �� �׷���� �ִ� ��츸 �׷� ���� ������� ǥ���Ѵ�.
	private ArrayList<String> groupOpenList;
	
	// JTable ���� ��ҵ�
	private String[] headers = {"", "Group", "Name", "URL", "Created Time", "Memo"};
	private DefaultTableModel model; 
	private JTable table;
	private JScrollPane scrollPane;
	
	// ������
	// BookmarkManager Ŭ�����κ��� BookmarkList�� ���� ȭ��ǥ�� �޾ƿ´�.
	// groupOpenList �� �ʱⰪ�� �����Ѵ�. �⺻(����Ʈ)�� ���� close �̹Ƿ�, open������ ���� �ƹ��͵� ����.
	// ��Ÿ ���� �⺻ ����, JTable, model, table, scrollPane ���� �⺻ ������ �ϸ�,
	// showBookmarks �� ���� �ʱ� �����͵��� �����ش�.
	BookmarkListPanel(BookmarkList bl){
		this.bl = bl;
		setLayout(new GridLayout());
		
		// groupOpenList - ����Ʈ�� ���� close
		groupOpenList = new ArrayList<String>();
		//groupOpenList.add("portal"); 

		// Jtable: model, table
		model = new DefaultTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);
		
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMinWidth(10);
		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(160);
		
		// group open, close �ϴ� �Ϳ� ���� mouseAdapter ����
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) { 
					JTable target = (JTable)e.getSource();
					if (target != table) return;
					
					if (table.getSelectedColumn() == 0) {
						int sRow = table.getSelectedRow();
						String openMark = (String) table.getValueAt(sRow, 0);
						String sGroup = (String) table.getValueAt(sRow, 1);
						if (openMark.equals(">")) {
							// �� ���� open ������ ���: close�� �����, �ٽ� showBookmarks 
							// mergeByGroup() �� �����ν�, up �̳� down���� ���� �Ͻ������� �̻������ ����ȭ 
							groupOpenList.add(sGroup);
							bl.mergeByGroup();
							showBookmarks();
						}
						else if (openMark.equals("V")) {
							// �� ���� close ������ ���: open�� �����, �ٽ� showBookmarks 
							// mergeByGroup() �� �����ν�, up �̳� down���� ���� �Ͻ������� �̻������ ����ȭ 
							groupOpenList.remove(sGroup);
							bl.mergeByGroup();
							showBookmarks();
						}
						else {
							// �� ���� ���� �ǹ̾��� Ŭ����.
							return;
						}
					}
				}
			}
		});
			
		
		// Jtable: scroll pane
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(650, 300));
		add(scrollPane);
		
		// showBookmarks
		showBookmarks();
		
		
		setVisible(true);
	}
	
	
	// bl�� �̿��ؼ�, ���� bookmark ���� JTable�� �����ִ� �޼ҵ�
	// ������ open , close ���¸� �����ϱ� ����, groupOpenList �ȿ� �ִ� �׷��鸸 open ���¸� ��������
	void showBookmarks() {
		// ���� table ���� �� ����
		int rowCount = model.getRowCount();
		while (rowCount != 0) {
			model.removeRow(0);
			rowCount--;
		}
		
		// groupName�� �ִ� bookmark�� �߰����� ��, �װ��� �� �׷��� ù��°�� ���, ù��°�� �ƴѰ�츦 �Ǵ� �ϱ� ���� ������
		String beforeGroupName = "";
		int beforeGroupState = -1; // �� -1:NoGroup, 0:closeGroup, 1:openGroup ��
		
		// bl�� �ִ� ��� �ϸ�ũ���� �ϳ��� �����ͼ� ó���Ѵ�.
		for (int i = 0; i<bl.numBookmarks(); i++) {
			Bookmark nowBM = bl.getBookmark(i);
			String nowGroupName = nowBM.getGroup();
			
			if (nowGroupName.equals("")) {
				// �� �׷� ����, �׳� �߰�
				model.addRow(new String[]{"", "", nowBM.getName(), nowBM.getUrl(),
						nowBM.getTime(), nowBM.getMemo()});
				
				// �׷��̸��� ���ٴ� ���� ǥ��
				beforeGroupName = "";
				beforeGroupState = -1;
			}
			else {
				// �� �׷� ����, �� �׷��� ù��° �߰����� �ƴ����� �켱 �����ؾ���
				
				if ((beforeGroupState == -1) || !(beforeGroupName.equals(nowGroupName))) {
					// �� ���� �߰��� bookmark�� �׷� �̸��� ���ų�, �ִ��� ���� groupName �̶� �ٸ� ���:
					// ��� �� �� �׷��� ù��° �߰�
					// open ���� close ���� �Ǵ��ؼ�, �׿� �°� �����͸� �߰��Ѵ�.
					// ���� beforeGroupName �� beforeGroupState ���� �˸°� ����.
					
					boolean isOpen = isOpenGroup(nowGroupName);
					if (isOpen == true) {
						// �׷��� open �����̰�, ù��°�̹Ƿ�, open ǥ���� V�� �׷� �̸��� ǥ��
						model.addRow(new String[]{"V", nowGroupName, "", "", "", ""});
						model.addRow(new String[]{"", nowGroupName, nowBM.getName(), nowBM.getUrl(),
								nowBM.getTime(), nowBM.getMemo()});
						beforeGroupName = nowGroupName;
						beforeGroupState = 1;
					}
					else {
						// �׷��� close �����̰�, ù��°�̹Ƿ�, close ǥ���� >�� �׷� �̸��� ǥ��
						model.addRow(new String[]{">", nowGroupName, "", "", "", ""});
						beforeGroupName = nowGroupName;
						beforeGroupState = 0;
					}
					
				}
				
				else {
					// ��� �� �� �׷��� ù��° �߰��� �ƴ� ���
					// �� �׷��� close ���� open ���� ����,
					// close �̸� pass, open �̸� bookmark �����͸� ǥ���Ѵ�.
					if (beforeGroupState == 1) {
						model.addRow(new String[]{"", nowGroupName, nowBM.getName(), nowBM.getUrl(),
								nowBM.getTime(), nowBM.getMemo()});
					}
				}
			}
		}
		
		model.fireTableDataChanged();
	}

	
	private boolean isOpenGroup(String groupName) {
		// ���ڷ� ���޹��� groupNamed��, groupOpenList�� �ִ��� �Ǵ��ϴ� �޼ҵ�
		// �ִ°�� (open�� ���) true, �ƴѰ�� false �� �����Ѵ�.
		
		for (int i = 0; i< groupOpenList.size(); i++) {
			if (groupOpenList.get(i).equals(groupName))
				return true;
		}
		return false;
	}
	
	public JTable getTable() {
		return table;
	}
	
//	private void addOpenGroup(String groupName) {
//		groupOpenList.add(groupName);
//	}
//	
//	private void removeOpenGroup(String groupName) {
//		groupOpenList.remove(groupName);
//	}
}
