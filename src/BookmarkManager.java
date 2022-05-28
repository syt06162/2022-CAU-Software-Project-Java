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
		// 사용자가 add 같은 작업을 할 경우, 우선 이 클래스(BookmarkManger) 내부 데이터가 갱신되고,
		// listPanel에서 JTable을 갱신하는데, 이때 이 bl의 화살표를 알고 있으므로 
		// 변경된 내부 데이터를 토대로 add 항목을 포함해 갱신할 수 있다. 
		listPanel = new BookmarkListPanel(bl);
		add(listPanel, BorderLayout.CENTER);
		
		// right - btnPanel
		btnPanel = new ButtonPanel();
		add(btnPanel, BorderLayout.EAST);
		
		setVisible(true);
	}
	
}
