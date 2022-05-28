import java.awt.BorderLayout;

import javax.swing.JFrame;

public class BookmarkManager extends JFrame{
	
	// ���� DB ���
	private BookmarkList bl;
	
	// ���� Panel : listPanel
	private BookmarkListPanel listPanel;
	
	// ���� Panel: btnPanel
	private ButtonPanel btnPanel;

	
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
		// left�� bl�� �Ѱ��༭, ȭ��ǥ(reference)�� �˵��� �ϰ� �Ѵ�. 
		// ����ڰ� add ���� �۾��� �� ���, �켱 �� Ŭ����(BookmarkManger) ���� �����Ͱ� ���ŵǰ�,
		// listPanel���� JTable�� �����ϴµ�, �̶� �� bl�� ȭ��ǥ�� �˰� �����Ƿ� 
		// ����� ���� �����͸� ���� add �׸��� ������ ������ �� �ִ�. 
		listPanel = new BookmarkListPanel(bl);
		add(listPanel, BorderLayout.CENTER);
		
		// right - btnPanel
		btnPanel = new ButtonPanel();
		add(btnPanel, BorderLayout.EAST);
		
		setVisible(true);
	}
	
}
