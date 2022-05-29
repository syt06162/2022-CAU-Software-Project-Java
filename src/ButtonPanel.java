import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
		
		setVisible(true);
		
	}
}
