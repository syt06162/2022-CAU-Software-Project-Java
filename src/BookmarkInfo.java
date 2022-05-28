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
	
	// input 버튼을 눌러 add를 한 경우, bmListPanel의 데이터를 갱신(showBookmarks() 메소드 호출) 및
	// bookmarkList 객체 (내부 데이터)에 데이터를 add 해야 한다. 
	// 따라서 <1> showBookmarks() 메소드를 갖고 있는 BookmarkListPanel 과,
	// <2> addBookmark() 메소드를 갖고 있는 BookmarkList 를 화살표로 갖고 있어야 한다.
	private BookmarkListPanel bmListPanel;
	private BookmarkList bl2;
	
	BookmarkInfo(BookmarkListPanel bmListPanel, BookmarkList bl){
		// 인자로 받은 bmListPanel, bl 추가
		this.bmListPanel = bmListPanel;
		this.bl2 = bl;
			
		// 기본 세팅
		setLayout(new BorderLayout());
		setTitle("Input New Bookmark");
		setSize(600, 100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// center에 input panel, east에 input btn
		inputPanel = new BookmarkInputPanel();
		add(inputPanel, BorderLayout.CENTER);
		
		// input에 Bookmark를 추가하는 클릭 리스너를 단다.
		inputBtn = new JButton("Input");
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputGroup = inputPanel.getTableValue(0, 0);
				String inputName = inputPanel.getTableValue(0, 1);
				String inputUrl = inputPanel.getTableValue(0, 2);
				String inputMemo = inputPanel.getTableValue(0, 3);
				
				if (inputUrl.equals("")) {
					// Bookmark 필수 항목인 url 입력을 안한 경우 경고 메시지
					JOptionPane.showMessageDialog(
							null, "url은 필수 입력 항목입니다.", "ADD 오류", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 기존 항목들중에 같은 url이 있어도 오류처리를 했음. 경고 메시지.
				for (int i = 0; i< bl2.numBookmarks(); i++) {
					if (bl2.getBookmark(i).getUrl().equals(inputUrl)) {
						JOptionPane.showMessageDialog(
								null, "중복된 url 입니다.", "ADD 오류", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				// url도 입력했고 중복도 아닌 경우 = 정상 입력
				// Bookmark를 추가해준다.
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
		
		// 빈줄 1개 add
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
	
	// 다른 클래스에서 table 자체의 값은 변경하지 못하게 table은 private 이지만,
	// table의 값에는 접근할 수 있도록 getter를 작성.
	// 이 메소드는 BookmarkInfo의 input 버튼에 부착되는 액션리스너에서
	// 사용자가 입력한 데이터들을 읽는 과정에서 사용된다.
	public String getTableValue(int row, int column) {
		return (String) table.getValueAt(row, column);
	}
	
}
