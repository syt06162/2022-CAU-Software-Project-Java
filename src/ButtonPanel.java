import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.GroupLayout.SequentialGroup;

public class ButtonPanel extends JPanel{
	JButton addBtn, deleteBtn, upBtn, downBtn, saveBtn;
	
	// BookmarkInfo 에 생성자로 전달해주기 위해
	// <1> showBookmarks() 메소드를 갖고 있는 BookmarkListPanel 과,
	// <2> addBookmark() 메소드를 갖고 있는 BookmarkList 를 화살표로 갖고 있어야 한다.
	private BookmarkListPanel bmListPanel;
	private BookmarkList bl;

	public ButtonPanel(BookmarkListPanel bmListPanel, BookmarkList bl) {
		setLayout(new GridLayout(5,1));
		this.bmListPanel = bmListPanel;
		this.bl = bl;
		
		// button들 생성
		addBtn = new JButton("ADD");
		deleteBtn = new JButton("DELETE");
		upBtn = new JButton("UP");
		downBtn = new JButton("DOWN");
		saveBtn = new JButton("SAVE");
		add(addBtn);
		add(deleteBtn);
		add(upBtn);
		add(downBtn);
		add(saveBtn);			
		
		// add 버튼 actionlistener
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookmarkInfo bi = new BookmarkInfo(bmListPanel, bl);				
			}
		});
		
		
		// delete 버튼 actionlistener
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sRow = bmListPanel.getTable().getSelectedRow(); // 사용자가 선택한 줄
				if (sRow == -1) {
				 // ■ 선택된 줄이 없는 경우 - 선택하라는 경고 dialog 표시
					JOptionPane.showMessageDialog(
							null, "삭제할 북마크를 선택해주세요.", "DELETE 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
					// ■ 선택된 줄이 있는 경우
					String marker = (String) bmListPanel.getTable().getValueAt(sRow, 0);
					if ((marker.equals(">")) || (marker.equals("V"))) {
						// ■■ 만약 선택된 줄이 marker인 경우, 즉 그룹을 열고닫는 marker인 경우
						//    그룹 말고 단일 북마크를 선택하라는 경고 dialog 표시
						JOptionPane.showMessageDialog(
								null, "그룹이 아닌 단일 북마크를 선택해주세요.", "DELETE 오류", JOptionPane.WARNING_MESSAGE);
						return;
					}
					// ■■ 선택된 줄이 단일 북마크인 경우
					// 해당 정보를 받아와서, 먼저 내부 데이터에서 삭제후,
					// JTable도 갱신
					// url 중복이 불가하게 만들었으므로, url 값이 일치하는 내부 데이터(Bookmark)를 삭제하면 됨
					String sUrl = (String) bmListPanel.getTable().getValueAt(sRow, 3);
					for (int i = 0; i< bl.numBookmarks(); i++) {
						if (bl.getBookmark(i).getUrl().equals(sUrl)) {
							bl.deleteBookmark(i);
							bl.mergeByGroup();
							bmListPanel.showBookmarks();
							return;
						}
					}
				}
			}
		});
		
		// UP 버튼 actionlistener
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = bmListPanel.getTable();
				int sRow = table.getSelectedRow(); // 사용자가 선택한 줄
				
				if (sRow == -1) {
				 // ■ 선택된 줄이 없는 경우 - 선택하라는 경고 dialog 표시
					JOptionPane.showMessageDialog(
							null, "위로 올릴 북마크를 선택해주세요.", "UP 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
				//  ■ 0번째줄이면 올릴게 없음.
					if (sRow == 0) return;
					
				//  ■ 선택된 줄이 있는 경우 - 상황에 맞게 올리기
					// 올리기 적절한 상황일 경우,
					
					// 올릴 대상이 close marker인 경우, JTable에서는 한칸 위로 가지만, 내부 데이터 상으로는 모든 그룹요소들을 한칸 올려줘야 함 
					// 		그러나 바로 위의 것도 close marker이면 모든 그룹 요소들을 위 그룹 요소 만큼 올려줘야 함
					// 올릴 대상이 open marker인 경우, JTable에서는 한칸 위로 가지만, 내부 데이터 상으로는 변화 없음
					// 올릴 대상이 그 외의 것인 경우, ...
					
					// 올릴 대상 바로 위의 것이 close marker이면 JTable에서는 한칸 위로 가지만, 내부 데이터 상으로는 close 그룹 bookmark 개수만큼 올리기
					// 올릴 대상 바로 위의 것이 open marker이면, JTblae에서는 한칸 위로 가지만, 내부 데이터 상으로는 변화 없음. (open marker는 bookmark가 아니므로)
					// 올릴 대상 바로 위의 것이 그 외인 경우, ...
					
					// 이렇듯 경우가 매우 복잡하니, 1. 우선 사용자가 올리려는것, 2. 올리려는 것 위의 것
					// 이 두가지를 파악한 후 상황을 분기
	
					// sMarker : 선택한 줄 marker,   sGroup : 선택한 줄 그룹,    sUrl : 선택한 줄 url
					String sMarker = (String)table.getValueAt(sRow, 0);
					String sGroup = (String)table.getValueAt(sRow, 1);
					String sUrl = (String)table.getValueAt(sRow, 3);
					
					// uMarker : 바로 윗줄 marker,   uGroup : 바로 윗줄 그룹
					String uMarker = (String)table.getValueAt(sRow-1, 0);
					String uGroup = (String)table.getValueAt(sRow-1, 1);
					
					
				//  ■■ JTable 상에서는 , 선택된것이 뭐든 관계없이 한개 올려줌
					swapTwoRowInTable(table, sRow, sRow-1);
					
					
				//  ■■ 선택된 줄이 close 그룹marker
					if (sMarker.equals(">")) {
						if (uMarker.equals("V")) {
							// ■■■ 바로 위의 것이 open marker,
							// 내부 데이터는 변화 없음.
							return;
						} 
						else if (uMarker.equals(">")) {
							// ■■■ 바로 위의 것이 close marker,
							// 내부 데이터에서는, 선택된 북마크 그룹의 요소들 각각을, 위의 그룹의 개수만큼 올려줌
							int upGroupCnt = bl.getGroupElementCount(uGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							int startIdx = bl.getGroupStartIndex(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								for (int j = 0; j<upGroupCnt; j++)
									bl.UPbookmark(startIdx + i - j);
							}
						}
						else {
							// ■■■ 바로 위의 것이 하나의 bookmark,
							// 내부 데이터에서는 그룹 전체를 올려줌 
							int startIdx = bl.getGroupStartIndex(sGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								bl.UPbookmark(startIdx + i);
							}
						}
						
					}
					
				//  ■■ 선택된 줄이 open 그룹marker
					else if (sMarker.equals("V")) {
						return;
					}
					
				//  ■■ 선택된 줄이 그 외의것
					else  {
						if (uMarker.equals("V")) {
							// ■■■ 바로 위의 것이 open marker,
							// 내부 데이터는 변화 없음.
							return;
						} 
						else if (uMarker.equals(">")) {
							// ■■■ 바로 위의 것이 close marker,
							// 내부 데이터에서는, 선택된 북마크를, 위의 그룹의 개수만큼 올려줌
							int upGroupCnt = bl.getGroupElementCount(uGroup);
							int startIdx = bl.getStartIndexWithUrl(sUrl);
							
							for (int i = 0; i<upGroupCnt; i++) {
								bl.UPbookmark(startIdx - i);
							}
						}
						else {
							// ■■■ 바로 위의 것이 하나의 bookmark,
							// 내부 데이터에서도 하나 올려줌 
							bl.UPbookmark(bl.getStartIndexWithUrl(sUrl));
						}
					}
				}
			}
		});
		
		// DOWN 버튼 actionlistener
		// upBtn의 리스너와 로직은 동일, 단 위 아래가 바뀐 것이라 생각하면 됨.
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = bmListPanel.getTable();
				int sRow = table.getSelectedRow(); // 사용자가 선택한 줄
				
				if (sRow == -1) {
				 // ■ 선택된 줄이 없는 경우 - 선택하라는 경고 dialog 표시
					JOptionPane.showMessageDialog(
							null, "아래로 내릴 북마크를 선택해주세요.", "DOWN 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
				//  ■ 마지막줄이면 내릴게 없음.
					if (sRow == table.getRowCount()) return;
					
					// sMarker : 선택한 줄 marker,   sGroup : 선택한 줄 그룹,    sUrl : 선택한 줄 url
					String sMarker = (String)table.getValueAt(sRow, 0);
					String sGroup = (String)table.getValueAt(sRow, 1);
					String sUrl = (String)table.getValueAt(sRow, 3);
					
					// dMarker : 바로 아랫줄 marker,   dGroup : 바로 아랫줄 그룹
					String dMarker = (String)table.getValueAt(sRow+1, 0);
					String dGroup = (String)table.getValueAt(sRow+1, 1);
					
					
				//  ■■ JTable 상에서는 , 선택된것이 뭐든 관계없이 한개 내려줌
					swapTwoRowInTable(table, sRow, sRow+1);
					
					
				//  ■■ 선택된 줄이 close 그룹marker
					if (sMarker.equals(">")) {
						if (dMarker.equals("V")) {
							// ■■■ 바로 아래의 것이 open marker,
							// 내부 데이터는 변화 없음.
							return;
						} 
						else if (dMarker.equals(">")) {
							// ■■■ 바로 아래의 것이 close marker,
							// 내부 데이터에서는, 선택된 북마크 그룹의 요소들 각각을, 아래의 그룹의 개수만큼 내려줌
							int downGroupCnt = bl.getGroupElementCount(dGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							int startIdx = bl.getGroupStartIndex(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								for (int j = 0; j<downGroupCnt; j++)
									bl.DOWNbookmark(startIdx + selectedGroupCnt - 1 - i + j);
							}
						}
						else {
							// ■■■ 바로 아래의 것이 하나의 bookmark,
							// 내부 데이터에서는 그룹 전체를 내려줌 
							int startIdx = bl.getGroupStartIndex(sGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							
							for (int i = selectedGroupCnt; i > 0; i--) {
								bl.DOWNbookmark(startIdx + i - 1);
							}
						}
						
					}
					
				//  ■■ 선택된 줄이 open 그룹marker
					else if (sMarker.equals("V")) {
						return;
					}
					
//					for (int i = 0 ; i<bl.numBookmarks(); i++)
//						System.out.println(bl.getBookmark(i).getName());
					
				//  ■■ 선택된 줄이 그 외의것
					else  {
						if (dMarker.equals("V")) {
							// ■■■ 바로 아래의 것이 open marker,
							// 내부 데이터는 변화 없음.
							return;
						} 
						else if (dMarker.equals(">")) {
							// ■■■ 바로 아래의 것이 close marker,
							// 내부 데이터에서는, 선택된 북마크를, 아래의 그룹의 개수만큼 내려줌
							int downGroupCnt = bl.getGroupElementCount(dGroup);
							int startIdx = bl.getStartIndexWithUrl(sUrl);
							
							for (int i = 0; i<downGroupCnt; i++) {
								bl.DOWNbookmark(startIdx + i);
							}
						}
						else {
							// ■■■ 바로 아래의 것이 하나의 bookmark,
							// 내부 데이터에서도 하나 내려줌
							bl.DOWNbookmark(bl.getStartIndexWithUrl(sUrl));
						}
					}
				}
			}
		});
		
		
		// save 버튼 actionlistener - 일단 show 하는걸로 @@@@
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bl.mergeByGroup();
				bmListPanel.showBookmarks();			
			}
		});
		
		setVisible(true);
		
	}
	
	
	// JTable 상에서 스왑해주는 메소드
	private void swapTwoRowInTable(JTable table, int sRow,  int nRow) {
		String[] sList = {(String)table.getValueAt(sRow, 0), (String)table.getValueAt(sRow, 1), (String)table.getValueAt(sRow, 2), 
				(String)table.getValueAt(sRow, 3), (String)table.getValueAt(sRow, 4), (String)table.getValueAt(sRow, 5) };
		String[] nList = {(String)table.getValueAt(nRow, 0), (String)table.getValueAt(nRow, 1), (String)table.getValueAt(nRow, 2), 
				(String)table.getValueAt(nRow, 3), (String)table.getValueAt(nRow, 4), (String)table.getValueAt(nRow, 5) };
		
		// swap
		for (int k = 0; k<sList.length; k++) {
			table.setValueAt(sList[k], nRow, k);
			table.setValueAt(nList[k], sRow, k);
		}
	}
}
