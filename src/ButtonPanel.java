import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	JButton addBtn, deleteBtn, upBtn, downBtn, saveBtn;
	
	// BookmarkInfo 에 생성자로 전달해주기 위해
	// <1> showBookmarks() 메소드를 갖고 있는 BookmarkListPanel 과,
	// <2> addBookmark() 메소드를 갖고 있는 BookmarkList 를 화살표로 갖고 있어야 한다.
//	private BookmarkListPanel bmListPanel;
//	private BookmarkList bl;

	public ButtonPanel(BookmarkListPanel bmListPanel, BookmarkList bl) {
		setLayout(new GridLayout(5,1));

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
		
		setVisible(true);
		
	}
}
