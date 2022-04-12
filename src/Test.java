import java.io.File;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// some test must be done in BookmarkList.java, but now here.
		

		BookmarkList bl = new BookmarkList("bgbg.txt");
		int num = bl.numBookmarks();
		for (int i = 0; i<num; i++)
			bl.getBookmark(i).print();
		
//		
//		Bookmark a = new Bookmark("www.naver.com");
//		a.print();
//		
//		Bookmark b = new Bookmark("d", "2012-02-00_12:23", "www.b", "group", "memo11");
//		b.print();
		
		
	}
}
