
public class Test {

	public static void main(String[] args) {
		
		BookmarkList bl = new BookmarkList("bgbg.txt");
		int num = bl.numBookmarks();
		for (int i = 0; i<num; i++)
			bl.getBookmark(i).print();

		
	}
}
