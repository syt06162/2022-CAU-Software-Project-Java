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
		// 잘못된 정보 처리
		// 1. 파일 이름 오류
		// 2. 파싱 필수인자 오류
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
			System.out.println("에러러러러");
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
