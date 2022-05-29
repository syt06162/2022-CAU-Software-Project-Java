import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	JButton addBtn, deleteBtn, upBtn, downBtn, saveBtn;
	
	// BookmarkInfo �� �����ڷ� �������ֱ� ����
	// <1> showBookmarks() �޼ҵ带 ���� �ִ� BookmarkListPanel ��,
	// <2> addBookmark() �޼ҵ带 ���� �ִ� BookmarkList �� ȭ��ǥ�� ���� �־�� �Ѵ�.
	private BookmarkListPanel bmListPanel;
	private BookmarkList bl;

	public ButtonPanel(BookmarkListPanel bmListPanel, BookmarkList bl) {
		setLayout(new GridLayout(5,1));
		this.bmListPanel = bmListPanel;
		this.bl = bl;
		
		// button�� ����
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
		
		// add ��ư actionlistener
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookmarkInfo bi = new BookmarkInfo(bmListPanel, bl);				
			}
		});
		
		
		// delete ��ư actionlistener
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sRow = bmListPanel.getTable().getSelectedRow(); // ����ڰ� ������ ��
				if (sRow == -1) {
				 // �� ���õ� ���� ���� ��� - �����϶�� ��� dialog ǥ��
					JOptionPane.showMessageDialog(
							null, "������ �ϸ�ũ�� �������ּ���.", "DELETE ����", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
					// �� ���õ� ���� �ִ� ���
					String marker = (String) bmListPanel.getTable().getValueAt(sRow, 0);
					if ((marker.equals(">")) || (marker.equals("V"))) {
						// ��� ���� ���õ� ���� marker�� ���, �� �׷��� ����ݴ� marker�� ���
						//    �׷� ���� ���� �ϸ�ũ�� �����϶�� ��� dialog ǥ��
						JOptionPane.showMessageDialog(
								null, "�׷��� �ƴ� ���� �ϸ�ũ�� �������ּ���.", "DELETE ����", JOptionPane.WARNING_MESSAGE);
						return;
					}
					// ��� ���õ� ���� ���� �ϸ�ũ�� ���
					// �ش� ������ �޾ƿͼ�, ���� ���� �����Ϳ��� ������,
					// JTable�� ����
					// url �ߺ��� �Ұ��ϰ� ��������Ƿ�, url ���� ��ġ�ϴ� ���� ������(Bookmark)�� �����ϸ� ��
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
