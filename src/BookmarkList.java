import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
					
					if (parsed.length!=5) {
						// ���� ����: �Ľ̵� �׸��� 5���� �ƴ�. 
						System.out.println("MalformedDateException: No 5 parsed ; invalid Bookmark info line: " + line);
					}
					else if (parsed[2].equals("")) {
						// ���� ����: �ʼ����(url) ����
						System.out.println("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: " + line);
					}
					else {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
							LocalDateTime dateAndTime = LocalDateTime.parse(parsed[1], formatter);
							
							// ���� ����: bookmark ���� �� �߰�
							bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]);
						} catch(DateTimeParseException e) {
							// ���� ����: Date ������ �߸���.
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
			
			for (int i = 0; i<bookmarkCount; i++) {
				printWriter.write(getBookmark(i).getStringFormat() + "\r\n");
			}
			printWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
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
