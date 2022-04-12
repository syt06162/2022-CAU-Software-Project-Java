import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class BookmarkList {
	private Bookmark[] bookmarkArray;
	private int bookmarkCount;
	
	BookmarkList(String bookmarkFileName){
		bookmarkArray = new Bookmark[100];
		addBookmarks(bookmarkFileName);
	}
	
	void addBookmarks(String bookmarkFileName) {
		Scanner input = null;
		String line = "";
		String[] parsed;
		
		int lineCount = 1; // ù��° �ٺ��� ����
		int addCount = 0; // ���� add ����
		int errCount = 0; // ���� ���� ����
		
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
					
					if (parsed.length!=5) {
						// ���� ����: �Ľ̵� �׸��� 5���� �ƴ�. 
						errCount++;
						System.out.println(lineCount + "��° �� ����: �Ľ̵� �׸��� 5���� �ƴմϴ�.");
					}
					else if (parsed[1]=="" && parsed[2]=="") {
						// ���� ����: �ʼ����(time, url) �Ѵ� ����
						errCount++;						
						System.out.println(lineCount + "��° �� ����: �ʼ����(time, url) �Ѵ� �����ϴ�.");
					}
					else if (parsed[1]=="") {
						// ���� ����: �ʼ����(time) ����
						errCount++;						
						System.out.println(lineCount + "��° �� ����: �ʼ����(time)�� �����ϴ�.");
					}
					else if (parsed[2]=="") {
						// ���� ����: �ʼ����(url) ����
						errCount++;						
						System.out.println(lineCount + "��° �� ����: �ʼ����(url)�� �����ϴ�.");
					}
					else {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
							LocalDateTime dateAndTime = LocalDateTime.parse(parsed[1], formatter);
							
							// ���� ����: bookmark ���� �� �߰�
							bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]);
							addCount++;
							
						} catch(DateTimeParseException e) {
							// ���� ����: �ð� ������ �߸���.
							errCount++;					
							System.out.println(lineCount + "��° �� ����: �ð� ������ �߸��Ǿ����ϴ�.");
							}
					}
				}
				
				lineCount++;
			}
			
			
			System.out.println("add: " + addCount + "��, error: " + errCount +"��" );
			
			
		}
		catch(FileNotFoundException e) {
			// e.printStackTrace();
			System.out.println("����: ������ ������ ã�� �� �����ϴ�.");
		}
	}
	
	public int numBookmarks() {
		return bookmarkCount;
	}
	
	public Bookmark getBookmark(int i) {
		return bookmarkArray[i];
	}
	
	public void mergeByGroup() {
		// to-do
	}
}
