import java.awt.BorderLayout;

import javax.swing.JFrame;

public class BookmarkManager extends JFrame{

	BookmarkManager(){
		setLayout(new BorderLayout());
		
		setTitle("Bookmark Manager");
		setSize(900, 350); // pack?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// left - listPanel
		BookmarkListPanel listPanel = new BookmarkListPanel();
		add(listPanel, BorderLayout.CENTER);
		
		
		// right - btnPanel
		ButtonPanel btnPanel = new ButtonPanel();
		add(btnPanel, BorderLayout.EAST);
		setVisible(true);
		
	}
	
}
