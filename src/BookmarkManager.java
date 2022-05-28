import java.awt.BorderLayout;

import javax.swing.JFrame;

public class BookmarkManager extends JFrame{
	
	// 내부 DB 기능
	private BookmarkList bl;
	
	// 좌측 Panel : listPanel
	private BookmarkListPanel listPanel;
	
	// 우측 Panel: btnPanel
	private ButtonPanel btnPanel;

	
	BookmarkManager(String fileName){
		// 1. bookmarkList 객체 생성
		bl = new BookmarkList(fileName);
		bl.mergeByGroup();
		
		// frame에 대한 layout
		setLayout(new BorderLayout());
		setTitle("Bookmark Manager");
		setSize(800, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// left - listPanel
		// left에 bl을 넘겨줘서, 화살표(reference)를 알도록 하게 한다. 
		// bl의 정보들을 이용해 showBookmarks() (JTable 갱신)을 할 수 있다.
		
		listPanel = new BookmarkListPanel(bl);
		add(listPanel, BorderLayout.CENTER);
		
		// right - btnPanel
		// btnPanel에서 사용자가 add 같은 작업을 할 경우, 
		// 우선 이 클래스(BookmarkManger) 내부 데이터(bl)가 갱신되고,
		// listPanel에서 JTable을 갱신(showBookmarks() 메소드를 통해)한다.
		// 따라서 이 두 작업을 위해, listPanel 및 bl의 화살표를 갖고 있어야 한다.
		btnPanel = new ButtonPanel(listPanel, bl);
		add(btnPanel, BorderLayout.EAST);
		
		setVisible(true);
	}
	
}
