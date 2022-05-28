import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkInfo extends JFrame{
	private BookmarkInputPanel inputPanel;
	private JButton inputBtn;
	
	// input ��ư�� ���� add�� �� ���, bmListPanel�� �����͸� ����(showBookmarks() �޼ҵ� ȣ��) ��
	// bookmarkList ��ü (���� ������)�� �����͸� add �ؾ� �Ѵ�. 
	// ���� <1> showBookmarks() �޼ҵ带 ���� �ִ� BookmarkListPanel ��,
	// <2> addBookmark() �޼ҵ带 ���� �ִ� BookmarkList �� ȭ��ǥ�� ���� �־�� �Ѵ�.
	private BookmarkListPanel bmListPanel;
	private BookmarkList bl2;
	
	BookmarkInfo(BookmarkListPanel bmListPanel, BookmarkList bl){
		// ���ڷ� ���� bmListPanel, bl �߰�
		this.bmListPanel = bmListPanel;
		this.bl2 = bl;
			
		// �⺻ ����
		setLayout(new BorderLayout());
		setTitle("Input New Bookmark");
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// center�� input panel, east�� input btn
		inputPanel = new BookmarkInputPanel();
		add(inputPanel, BorderLayout.CENTER);
		
		// input�� Bookmark�� �߰��ϴ� Ŭ�� �����ʸ� �ܴ�.
		inputBtn = new JButton("Input");
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputGroup = inputPanel.getTableValue(0, 0);
				String inputName = inputPanel.getTableValue(0, 1);
				String inputUrl = inputPanel.getTableValue(0, 2);
				String inputMemo = inputPanel.getTableValue(0, 3);
				
				if (inputUrl.equals("")) {
					// Bookmark �ʼ� �׸��� url �Է��� ���� ��� ��� �޽���
					JOptionPane.showMessageDialog(
							null, "url�� �ʼ� �Է� �׸��Դϴ�.", "ADD ����", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// ���� �׸���߿� ���� url�� �־ ����ó���� ����. ��� �޽���.
				for (int i = 0; i< bl2.numBookmarks(); i++) {
					if (bl2.getBookmark(i).getUrl().equals(inputUrl)) {
						JOptionPane.showMessageDialog(
								null, "�ߺ��� url �Դϴ�.", "ADD ����", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				// url�� �Է��߰� �ߺ��� �ƴ� ��� = ���� �Է�
				// Bookmark�� �߰����ش�.
				Bookmark bm = new Bookmark( inputName, inputUrl, inputGroup, inputMemo);
				bl2.addBookmark(bm);
				bl2.mergeByGroup();
				bmListPanel.showBookmarks();
				dispose();
			}
		});
		add(inputBtn, BorderLayout.EAST);
		
		
		setVisible(true);
	}
}

class BookmarkInputPanel extends JPanel{
	private String[] headers = {"Group", "Name", "URL", "Memo"};
	private DefaultTableModel model; 
	private JTable table;
	private JScrollPane scrollPane;
	
	BookmarkInputPanel(){
		setLayout(new GridLayout());
		// Jtable, model
		model = new DefaultTableModel();
		model.setColumnCount(headers.length);
		model.setColumnIdentifiers(headers);
		
		// ���� 1�� add
		model.addRow(new String[]{"","","",""});
		model.fireTableDataChanged();
		
		table = new JTable(model);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
				
		// scroll pane
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(650, 300));
		add(scrollPane);
		
		setVisible(true);
	}
	
	// �ٸ� Ŭ�������� table ��ü�� ���� �������� ���ϰ� table�� private ������,
	// table�� ������ ������ �� �ֵ��� getter�� �ۼ�.
	// �� �޼ҵ�� BookmarkInfo�� input ��ư�� �����Ǵ� �׼Ǹ����ʿ���
	// ����ڰ� �Է��� �����͵��� �д� �������� ���ȴ�.
	public String getTableValue(int row, int column) {
		return (String) table.getValueAt(row, column);
	}
	
}
