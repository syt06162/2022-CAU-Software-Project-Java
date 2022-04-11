import java.io.File;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// some test must be done in BookmarkList.java, but now here.
		
		File file = new File("bgbg.txt");
		Scanner input = null;
		String line = "";
		String[] parsed;
		
		try {
			input = new Scanner(file);
			while (input.hasNext()) {
				line = input.nextLine().trim();
				if (line == "") {
					System.out.println("빈줄");
				}
				else if (line.substring(0, 2).equals("//")) {
					System.out.println("주석");
				}
				else {
					System.out.println("파싱");
					parsed = line.split("[,;]", -1); 
					for (int i = 0; i<parsed.length; i++)
						parsed[i] = parsed[i].trim();
					System.out.println(parsed[0] +parsed[1] +parsed[2] + parsed[3]+parsed[4]); 
				}
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("에러러러러");
		}
		
//		
//		Bookmark a = new Bookmark("www.naver.com");
//		a.print();
//		
//		Bookmark b = new Bookmark("d", "2012-02-00_12:23", "www.b", "group", "memo11");
//		b.print();
		
		
	}
}
