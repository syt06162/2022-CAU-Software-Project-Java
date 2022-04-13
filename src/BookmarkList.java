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
	private int bookmarkCount; // bookmark ����
	private final int MAX_SIZE = 100;
	
	BookmarkList(String bookmarkFileName){
		bookmarkArray = new Bookmark[MAX_SIZE];
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
							bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]);
							
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
			for (int i = 0; i<bookmarkCount; i++) {
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
		return bookmarkCount;
	}
	
	public Bookmark getBookmark(int i) {
		// �Է°� i ��° bookmark ����
		return bookmarkArray[i];
	}
	
	public void mergeByGroup() {
		// ���� : 0���� ���������� �ö󰡸鼭, group�̸��� �ִ°��� ã�´�.
		// group�̸��� ã�Ҵٸ�, �׵ڿ� ������ bookmark �� �� group�̸��� ���� group�̸��� ���� bookmark����, 
		// ���ο� array�� ������ �����ϸ� �߰��Ѵ�.
		// �߰��� ���� ������ �����ؼ�, ������ Ž���� �� ���� �ʰ� �Ѵ�.
		
		// �̷��� �� �ö󰡸鼭 group�̸��� �ִ°͵��� �� ���ο� �迭�� �߰��Ѵ�.
		// ���������� �ٽ� 0������ Ž���� group�̸��� ���°͵��� ���ο� �迭 �޺κп� �߰����ش�.
		
		Bookmark[] newArray = new Bookmark[MAX_SIZE];
		int oldPointer = 0; // ���� �迭��, ���� Ž������ �ε����� ����Ű�� ��
		int newPointer = 0; // ���ο� �迭��, �߰��Ǿ�� �ϴ� ���� �ε����� ����Ű�� ��
		String groupName = null; // �׷� �̸��� ã���� ���⿡ ����ǰ�, �׵ڷ� �� �̸��� ���� �̸��� ���� bookmark�� ���ο� �迭�� �߰��Ѵ�.
		
		while (oldPointer < bookmarkCount) {
			int tempPointer; // group�� ã���������� �׵ڷ� Ž���Ҷ� ����� ������ 

			if (bookmarkArray[oldPointer] != null && !bookmarkArray[oldPointer].getGroup().equals("")) {
				groupName = bookmarkArray[oldPointer].getGroup();
				tempPointer = oldPointer; // oldPointer ������ ������ Ž���Ѵ�.
				while (tempPointer < bookmarkCount) {
					if (bookmarkArray[tempPointer] != null && bookmarkArray[tempPointer].getGroup().equals(groupName)){
						// ���� ã�� groupName �� ���� group�̸�, �̰��� ���ο� �迭�� �߰��Ѵ�.
						// �׸��� ���� �迭�� ������ null�� �����, ������ Ž������ �� �ɸ��� �ʰ� �Ѵ�.
						newArray[newPointer] = bookmarkArray[tempPointer];
						bookmarkArray[tempPointer] = null;
						newPointer++;
					}
					tempPointer++;
				}
				
			}
			oldPointer++;
		}
		// ������� �Ǹ� �׷��̸��� �ִ� �͵��� ��� �Ű�����.
		
		oldPointer = 0;
		// ���� �׷��̸��� ���� �͵��� �տ������� �ϳ��ϳ� ���ο� �迭�� �Ű��ش�.
		while (oldPointer < bookmarkCount) {
			if (bookmarkArray[oldPointer] != null && bookmarkArray[oldPointer].getGroup().equals("")) {
				newArray[newPointer] = bookmarkArray[oldPointer];
				bookmarkArray[oldPointer] = null;
				newPointer++;
			}
			oldPointer++;
		}
		
		// ���ο� �迭�� merge�� �Ϸ�Ǿ����Ƿ�, ���� ������ ���ο� �迭�� �Ű��ش�.
		bookmarkArray = newArray;
		
	}
}
