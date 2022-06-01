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
	
	// BookmarkManager 에서 관리되고 있는 내부 데이터인 BookmarkList 에 대한 화살표
	// 이를 알고 있으므로, showBookmarks 에 의해 모든 bookmark들을 성공적으로 불러올 수 있다.
	private BookmarkList bl;
	
	// JTable에서, 현재 open상태인 그룹 이름들을 저장하는 리스트.
	// showBookmarks 메소드 실행 시, 이 리스트에 그 그룹명이 있는 경우만 그룹 안의 내용들을 표시한다.
	private ArrayList<String> groupOpenList;
	
	// JTable 관련 요소들
	private String[] headers = {"", "Group", "Name", "URL", "Created Time", "Memo"};
	private DefaultTableModel model; 
	private JTable table;
	private JScrollPane scrollPane;
	
	// 생성자
	// BookmarkManager 클래스로부터 BookmarkList에 대한 화살표를 받아온다.
	// groupOpenList 의 초기값을 세팅한다. 기본(디폴트)은 전부 close 이므로, open상태인 것은 아무것도 없다.
	// 기타 각종 기본 설정, JTable, model, table, scrollPane 등의 기본 설정을 하며,
	// showBookmarks 를 통해 초기 데이터들을 보여준다.
	BookmarkListPanel(BookmarkList bl){
		this.bl = bl;
		setLayout(new GridLayout());
		
		// groupOpenList - 디폴트는 전부 close
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
		
		// group open, close 하는 것에 대한 mouseAdapter 부착
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
							// ■ 현재 open 상태인 경우: close로 만들고, 다시 showBookmarks 
							// mergeByGroup() 도 함으로써, up 이나 down으로 인해 일시적으로 이상해졌어도 정상화 
							groupOpenList.add(sGroup);
							bl.mergeByGroup();
							showBookmarks();
						}
						else if (openMark.equals("V")) {
							// ■ 현재 close 상태인 경우: open로 만들고, 다시 showBookmarks 
							// mergeByGroup() 도 함으로써, up 이나 down으로 인해 일시적으로 이상해졌어도 정상화 
							groupOpenList.remove(sGroup);
							bl.mergeByGroup();
							showBookmarks();
						}
						else {
							// 그 외의 경우는 의미없는 클릭임.
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
	
	
	// bl을 이용해서, 현재 bookmark 들을 JTable에 보여주는 메소드
	// 기존에 open , close 상태를 유지하기 위해, groupOpenList 안에 있는 그룹명들만 open 상태를 유지해줌
	void showBookmarks() {
		// 기존 table 정보 다 삭제
		int rowCount = model.getRowCount();
		while (rowCount != 0) {
			model.removeRow(0);
			rowCount--;
		}
		
		// groupName이 있는 bookmark를 발견했을 때, 그것이 그 그룹의 첫번째인 경우, 첫번째가 아닌경우를 판단 하기 위한 변수들
		String beforeGroupName = "";
		int beforeGroupState = -1; // ■ -1:NoGroup, 0:closeGroup, 1:openGroup ■
		
		// bl에 있는 모든 북마크들을 하나씩 가져와서 처리한다.
		for (int i = 0; i<bl.numBookmarks(); i++) {
			Bookmark nowBM = bl.getBookmark(i);
			String nowGroupName = nowBM.getGroup();
			
			if (nowGroupName.equals("")) {
				// ■ 그룹 없음, 그냥 추가
				model.addRow(new String[]{"", "", nowBM.getName(), nowBM.getUrl(),
						nowBM.getTime(), nowBM.getMemo()});
				
				// 그룹이름이 없다는 정보 표시
				beforeGroupName = "";
				beforeGroupState = -1;
			}
			else {
				// ■ 그룹 있음, 그 그룹의 첫번째 발견인지 아닌지를 우선 판정해야함
				
				if ((beforeGroupState == -1) || !(beforeGroupName.equals(nowGroupName))) {
					// 그 전에 추가한 bookmark가 그룹 이름이 없거나, 있더라도 지금 groupName 이랑 다른 경우:
					// ■■ 즉 그 그룹의 첫번째 발견
					// open 인지 close 인지 판단해서, 그에 맞게 데이터를 추가한다.
					// 그후 beforeGroupName 과 beforeGroupState 값을 알맞게 설정.
					
					boolean isOpen = isOpenGroup(nowGroupName);
					if (isOpen == true) {
						// 그룹이 open 상태이고, 첫번째이므로, open 표시인 V와 그룹 이름만 표시
						model.addRow(new String[]{"V", nowGroupName, "", "", "", ""});
						model.addRow(new String[]{"", nowGroupName, nowBM.getName(), nowBM.getUrl(),
								nowBM.getTime(), nowBM.getMemo()});
						beforeGroupName = nowGroupName;
						beforeGroupState = 1;
					}
					else {
						// 그룹이 close 상태이고, 첫번째이므로, close 표시인 >와 그룹 이름만 표시
						model.addRow(new String[]{">", nowGroupName, "", "", "", ""});
						beforeGroupName = nowGroupName;
						beforeGroupState = 0;
					}
					
				}
				
				else {
					// ■■ 즉 그 그룹의 첫번째 발견이 아닌 경우
					// 그 그룹이 close 인지 open 인지 봐서,
					// close 이면 pass, open 이면 bookmark 데이터를 표시한다.
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
		// 인자로 전달받은 groupNamed이, groupOpenList에 있는지 판단하는 메소드
		// 있는경우 (open인 경우) true, 아닌경우 false 를 리턴한다.
		
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
