import java.awt.BorderLayout;

import javax.swing.JFrame;

public class BookmarkManager extends JFrame{

	BookmarkList bl;
	
	BookmarkManager(String fileName){
		// 1. bookmarkList ��ü ����
		bl = new BookmarkList(fileName);
		bl.mergeByGroup();
		
		// frame�� ���� layout
		setLayout(new BorderLayout());
		setTitle("Bookmark Manager");
		setSize(800, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// left - listPanel
		BookmarkListPanel listPanel = new BookmarkListPanel(bl);
		add(listPanel, BorderLayout.CENTER);
	
		
		// right - btnPanel
		ButtonPanel btnPanel = new ButtonPanel();
		add(btnPanel, BorderLayout.EAST);
		
		setVisible(true);
	}
	
}
