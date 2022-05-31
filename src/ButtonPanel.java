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
		
		// UP ��ư actionlistener
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = bmListPanel.getTable();
				int sRow = table.getSelectedRow(); // ����ڰ� ������ ��
				
				if (sRow == -1) {
				 // �� ���õ� ���� ���� ��� - �����϶�� ��� dialog ǥ��
					JOptionPane.showMessageDialog(
							null, "���� �ø� �ϸ�ũ�� �������ּ���.", "UP ����", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
				//  �� 0��°���̸� �ø��� ����.
					if (sRow == 0) return;
					
				//  �� ���õ� ���� �ִ� ��� - ��Ȳ�� �°� �ø���
					// �ø��� ������ ��Ȳ�� ���,
					
					// �ø� ����� close marker�� ���, JTable������ ��ĭ ���� ������, ���� ������ �����δ� ��� �׷��ҵ��� ��ĭ �÷���� �� 
					// 		�׷��� �ٷ� ���� �͵� close marker�̸� ��� �׷� ��ҵ��� �� �׷� ��� ��ŭ �÷���� ��
					// �ø� ����� open marker�� ���, JTable������ ��ĭ ���� ������, ���� ������ �����δ� ��ȭ ����
					// �ø� ����� �� ���� ���� ���, ...
					
					// �ø� ��� �ٷ� ���� ���� close marker�̸� JTable������ ��ĭ ���� ������, ���� ������ �����δ� close �׷� bookmark ������ŭ �ø���
					// �ø� ��� �ٷ� ���� ���� open marker�̸�, JTblae������ ��ĭ ���� ������, ���� ������ �����δ� ��ȭ ����. (open marker�� bookmark�� �ƴϹǷ�)
					// �ø� ��� �ٷ� ���� ���� �� ���� ���, ...
					
					// �̷��� ��찡 �ſ� �����ϴ�, 1. �켱 ����ڰ� �ø����°�, 2. �ø����� �� ���� ��
					// �� �ΰ����� �ľ��� �� ��Ȳ�� �б�
	
					// sMarker : ������ �� marker,   sGroup : ������ �� �׷�,    sUrl : ������ �� url
					String sMarker = (String)table.getValueAt(sRow, 0);
					String sGroup = (String)table.getValueAt(sRow, 1);
					String sUrl = (String)table.getValueAt(sRow, 3);
					
					// uMarker : �ٷ� ���� marker,   uGroup : �ٷ� ���� �׷�
					String uMarker = (String)table.getValueAt(sRow-1, 0);
					String uGroup = (String)table.getValueAt(sRow-1, 1);
					
					
				//  ��� JTable �󿡼��� , ���õȰ��� ���� ������� �Ѱ� �÷���
					swapTwoRowInTable(table, sRow, sRow-1);
					
					
				//  ��� ���õ� ���� close �׷�marker
					if (sMarker.equals(">")) {
						if (uMarker.equals("V")) {
							// ���� �ٷ� ���� ���� open marker,
							// ���� �����ʹ� ��ȭ ����.
							return;
						} 
						else if (uMarker.equals(">")) {
							// ���� �ٷ� ���� ���� close marker,
							// ���� �����Ϳ�����, ���õ� �ϸ�ũ �׷��� ��ҵ� ������, ���� �׷��� ������ŭ �÷���
							int upGroupCnt = bl.getGroupElementCount(uGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							int startIdx = bl.getGroupStartIndex(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								for (int j = 0; j<upGroupCnt; j++)
									bl.UPbookmark(startIdx + i - j);
							}
						}
						else {
							// ���� �ٷ� ���� ���� �ϳ��� bookmark,
							// ���� �����Ϳ����� �׷� ��ü�� �÷��� 
							int startIdx = bl.getGroupStartIndex(sGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								bl.UPbookmark(startIdx + i);
							}
						}
						
					}
					
				//  ��� ���õ� ���� open �׷�marker
					else if (sMarker.equals("V")) {
						return;
					}
					
				//  ��� ���õ� ���� �� ���ǰ�
					else  {
						if (uMarker.equals("V")) {
							// ���� �ٷ� ���� ���� open marker,
							// ���� �����ʹ� ��ȭ ����.
							return;
						} 
						else if (uMarker.equals(">")) {
							// ���� �ٷ� ���� ���� close marker,
							// ���� �����Ϳ�����, ���õ� �ϸ�ũ��, ���� �׷��� ������ŭ �÷���
							int upGroupCnt = bl.getGroupElementCount(uGroup);
							int startIdx = bl.getStartIndexWithUrl(sUrl);
							
							for (int i = 0; i<upGroupCnt; i++) {
								bl.UPbookmark(startIdx - i);
							}
						}
						else {
							// ���� �ٷ� ���� ���� �ϳ��� bookmark,
							// ���� �����Ϳ����� �ϳ� �÷��� 
							bl.UPbookmark(bl.getStartIndexWithUrl(sUrl));
						}
					}
				}
			}
		});
		
		// DOWN ��ư actionlistener
		// upBtn�� �����ʿ� ������ ����, �� �� �Ʒ��� �ٲ� ���̶� �����ϸ� ��.
		downBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = bmListPanel.getTable();
				int sRow = table.getSelectedRow(); // ����ڰ� ������ ��
				
				if (sRow == -1) {
				 // �� ���õ� ���� ���� ��� - �����϶�� ��� dialog ǥ��
					JOptionPane.showMessageDialog(
							null, "�Ʒ��� ���� �ϸ�ũ�� �������ּ���.", "DOWN ����", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
				//  �� ���������̸� ������ ����.
					if (sRow == table.getRowCount()) return;
					
					// sMarker : ������ �� marker,   sGroup : ������ �� �׷�,    sUrl : ������ �� url
					String sMarker = (String)table.getValueAt(sRow, 0);
					String sGroup = (String)table.getValueAt(sRow, 1);
					String sUrl = (String)table.getValueAt(sRow, 3);
					
					// dMarker : �ٷ� �Ʒ��� marker,   dGroup : �ٷ� �Ʒ��� �׷�
					String dMarker = (String)table.getValueAt(sRow+1, 0);
					String dGroup = (String)table.getValueAt(sRow+1, 1);
					
					
				//  ��� JTable �󿡼��� , ���õȰ��� ���� ������� �Ѱ� ������
					swapTwoRowInTable(table, sRow, sRow+1);
					
					
				//  ��� ���õ� ���� close �׷�marker
					if (sMarker.equals(">")) {
						if (dMarker.equals("V")) {
							// ���� �ٷ� �Ʒ��� ���� open marker,
							// ���� �����ʹ� ��ȭ ����.
							return;
						} 
						else if (dMarker.equals(">")) {
							// ���� �ٷ� �Ʒ��� ���� close marker,
							// ���� �����Ϳ�����, ���õ� �ϸ�ũ �׷��� ��ҵ� ������, �Ʒ��� �׷��� ������ŭ ������
							int downGroupCnt = bl.getGroupElementCount(dGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							int startIdx = bl.getGroupStartIndex(sGroup);
							
							for (int i = 0; i<selectedGroupCnt; i++) {
								for (int j = 0; j<downGroupCnt; j++)
									bl.DOWNbookmark(startIdx + selectedGroupCnt - 1 - i + j);
							}
						}
						else {
							// ���� �ٷ� �Ʒ��� ���� �ϳ��� bookmark,
							// ���� �����Ϳ����� �׷� ��ü�� ������ 
							int startIdx = bl.getGroupStartIndex(sGroup);
							int selectedGroupCnt = bl.getGroupElementCount(sGroup);
							
							for (int i = selectedGroupCnt; i > 0; i--) {
								bl.DOWNbookmark(startIdx + i - 1);
							}
						}
						
					}
					
				//  ��� ���õ� ���� open �׷�marker
					else if (sMarker.equals("V")) {
						return;
					}
					
//					for (int i = 0 ; i<bl.numBookmarks(); i++)
//						System.out.println(bl.getBookmark(i).getName());
					
				//  ��� ���õ� ���� �� ���ǰ�
					else  {
						if (dMarker.equals("V")) {
							// ���� �ٷ� �Ʒ��� ���� open marker,
							// ���� �����ʹ� ��ȭ ����.
							return;
						} 
						else if (dMarker.equals(">")) {
							// ���� �ٷ� �Ʒ��� ���� close marker,
							// ���� �����Ϳ�����, ���õ� �ϸ�ũ��, �Ʒ��� �׷��� ������ŭ ������
							int downGroupCnt = bl.getGroupElementCount(dGroup);
							int startIdx = bl.getStartIndexWithUrl(sUrl);
							
							for (int i = 0; i<downGroupCnt; i++) {
								bl.DOWNbookmark(startIdx + i);
							}
						}
						else {
							// ���� �ٷ� �Ʒ��� ���� �ϳ��� bookmark,
							// ���� �����Ϳ����� �ϳ� ������
							bl.DOWNbookmark(bl.getStartIndexWithUrl(sUrl));
						}
					}
				}
			}
		});
		
		
		// save ��ư actionlistener - �ϴ� show �ϴ°ɷ� @@@@
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bl.mergeByGroup();
				bmListPanel.showBookmarks();			
			}
		});
		
		setVisible(true);
		
	}
	
	
	// JTable �󿡼� �������ִ� �޼ҵ�
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
