import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookmarkList {
	private ArrayList<Bookmark> bookmarkArray;
	
	BookmarkList(String bookmarkFileName){
		bookmarkArray = new ArrayList<Bookmark>();
		loadBookmarks(bookmarkFileName); 
	}
	
	void loadBookmarks(String bookmarkFileName) {
		// bookmarkFileName �� �Է¹޾�, �� ���� �ϸ�ũ ������ �Ľ��� �ϸ�ũ�� �߰��ϴ� �޼ҵ�
		
		Scanner input = null;
		String line = "";
		String[] parsed;
		
		try {
			File file = new File(bookmarkFileName);
			input = new Scanner(file);
			
			while (input.hasNext()) {
				line = input.nextLine().trim();

				if (line != "" && !(line.substring(0, 2).equals("//"))) {
					// ������ �ƴϰ�, �ּ��ٵ� �ƴҶ� => �Ľ� ����
					
					parsed = line.split("[,;]", -1); 
					for (int i = 0; i<parsed.length; i++)
						parsed[i] = parsed[i].trim();
					
					// �Ľ��� �Ϸ�Ǿ�����, �������� �Ǵ��Ͽ� �߰����� ���� ����.
					// 1.url�� �ִ��� ; 2.date������ �ùٸ���
					if (parsed[2].equals("")) {
						// ���� ���� 1: �ʼ����(url) ����
						System.out.println("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: " + line);
					}
					else {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
							LocalDateTime dateAndTime = LocalDateTime.parse(parsed[1], formatter);
							// ���� ����: bookmark ���� �� �߰�
							bookmarkArray.add(new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]));
							
						} catch(DateTimeParseException e) {
							// ���� ���� 2: Date ������ �߸���.
							System.out.println("Date Format Error -> No Created Time invalid Bookmark info line: " + line);
						}
					}
				}
				
			}
			
			
		}
		catch(FileNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Unknwon BookmarkList data File");
		}
	}
	
	void saveBookmarks(String bookmarkFileName) {
		// BookmarkList�� ����Ǿ� �ִ� ��� Bookmark����, ���Ŀ� �°� ���Ͽ� ����
		// �Է¹��� ���� �̸��� ���� ����
		File file = new File(bookmarkFileName);
		
		try {
		    FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			
			printWriter.write("// Bookmark information - text" + "\r\n");
			for (int i = 0; i< numBookmarks(); i++) {
				printWriter.write(getBookmark(i).getStringFormat() + "\r\n");
			}
			printWriter.write("// end of Bookmark information - text" + "\r\n");
			printWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}

	}
	
	public int numBookmarks() {
		// bookmark ���� ����
		return bookmarkArray.size();
	}
	
	public Bookmark getBookmark(int i) {
		// �Է°� i ��° bookmark ����
		return bookmarkArray.get(i);
	}
	
	public void mergeByGroup() {
		// *���� 5* Bookmark[] �� ����ϴ� �Ϳ��� arrayList ������� �����Ͽ���. 
		// ���� ��� �����Ͱ� �����ǰ�, add, remove �޼ҵ�� ��ü
		
		// ���� : 0���� ���������� �ö󰡸鼭, group�̸��� �ִ°��� ã�´�.
		// group�̸��� ã�Ҵٸ�, �׵ڿ� ������ bookmark �� �� group�̸��� ���� group�̸��� ���� bookmark����, 
		// ���ο� array�� ������ �����ϸ� �߰��Ѵ�.
		// �߰��� ���� remove �Ѵ�.
		
		// �̷��� �� �ö󰡸鼭 group�̸��� �ִ°͵��� �� ���ο� �迭�� �߰��Ѵ�.
		// �̶�, �׷��̸��� ���� �͵��� �׳� ������ �����ϵ��� ���ο� �迭�� �߰��ϸ� �ȴ�.
		
		ArrayList<Bookmark> newArray = new ArrayList<Bookmark>();
		String groupName = null; // �׷� �̸��� ã���� ���⿡ ����ǰ�, �׵ڷ� �� �̸��� ���� �̸��� ���� bookmark�� ���ο� �迭�� �߰��Ѵ�.
		
		while (numBookmarks() != 0) {
			int tempPointer; // group�� ã���������� �׵ڷ� Ž���Ҷ� ����� ������ 
			
			if (bookmarkArray.get(0).getGroup().equals("")) {
				// �׷��̸��� ���°��� ������ ���� ���� newArray�� �׳� �߰��Ѵ�.
				newArray.add(bookmarkArray.get(0));
				bookmarkArray.remove(0);
			}
			else {
				// �׷��̸��� �ִ�!
				// ���Ŀ� ������ ���� �׷��̸� Bookmark���� ��� ���ο� �迭�� �߰��Ѵ�.
				groupName = bookmarkArray.get(0).getGroup();
				tempPointer = 0;
				while (tempPointer < numBookmarks()) {
					if (bookmarkArray.get(tempPointer).getGroup().equals(groupName)){
						// ���� ã�� groupName �� ���� group�̸�, �̰��� ���ο� �迭�� �߰��Ѵ�.
						// �׸��� ���� �迭�� ���� remove
						newArray.add(bookmarkArray.get(tempPointer));
						bookmarkArray.remove(tempPointer);
					}
					else {
						tempPointer++;
					}
				}
			}
		}
		// ���ο� �迭�� merge�� �Ϸ�Ǿ����Ƿ�, ���� ������ ���ο� �迭�� �Ű��ش�.
		bookmarkArray = newArray;
	}
	
	// ���� 6 �߰� ���� @@@@
	void addBookmark(Bookmark bm) {
		bookmarkArray.add(bm);
	}
	private void deleteBookmark(Bookmark bm) {
		bookmarkArray.remove(bm);
	}
}
