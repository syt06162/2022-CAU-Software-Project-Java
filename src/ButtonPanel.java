import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	JButton addBtn, deleteBtn, upBtn, downBtn, saveBtn;

	public ButtonPanel() {
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
				BookmarkInfo bi = new BookmarkInfo();				
			}
		});
		
		setVisible(true);
		
	}
}
