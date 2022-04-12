import java.io.File;
import java.util.Scanner;

public class BookmarkList {
	private Bookmark[] bookmarkArray;
	private int bookmarkCount;
	
	BookmarkList(String bookmarkFileName){
		bookmarkArray = new Bookmark[100];
		addBookmarks(bookmarkFileName);
	}
	
	void addBookmarks(String bookmarkFileName) {
		// �߸��� ���� ó��
		// 1. ���� �̸� ����
		// 2. �Ľ� �ʼ����� ����
		File file = new File(bookmarkFileName);
		Scanner input = null;
		String line = "";
		String[] parsed;
		try {
			input = new Scanner(file);
			while (input.hasNext()) {
				line = input.nextLine().trim();
				if (line == "" || line.substring(0, 2).equals("//")) {
					continue;
				}
				else {
					parsed = line.split("[,;]", -1); 
					for (int i = 0; i<parsed.length; i++)
						parsed[i] = parsed[i].trim();
					bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], parsed[1], parsed[2], parsed[3], parsed[4]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("����������");
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
