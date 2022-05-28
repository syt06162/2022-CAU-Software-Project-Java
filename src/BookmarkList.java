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
		// bookmarkFileName 을 입력받아, 줄 별로 북마크 정보를 파싱해 북마크를 추가하는 메소드
		
		Scanner input = null;
		String line = "";
		String[] parsed;
		
		try {
			File file = new File(bookmarkFileName);
			input = new Scanner(file);
			
			while (input.hasNext()) {
				line = input.nextLine().trim();

				if (line != "" && !(line.substring(0, 2).equals("//"))) {
					// 빈줄이 아니고, 주석줄도 아닐때 => 파싱 시작
					
					parsed = line.split("[,;]", -1); 
					for (int i = 0; i<parsed.length; i++)
						parsed[i] = parsed[i].trim();
					
					// 파싱이 완료되었으니, 오류인지 판단하여 추가할지 말지 결정.
					// 1.url이 있는지 ; 2.date형식이 올바른지
					if (parsed[2].equals("")) {
						// 에러 유형 1: 필수요소(url) 없음
						System.out.println("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: " + line);
					}
					else {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
							LocalDateTime dateAndTime = LocalDateTime.parse(parsed[1], formatter);
							// 정상 라인: bookmark 생성 및 추가
							bookmarkArray.add(new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]));
							
						} catch(DateTimeParseException e) {
							// 에러 유형 2: Date 형식이 잘못됨.
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
		// BookmarkList에 저장되어 있는 모든 Bookmark들을, 형식에 맞게 파일에 저장
		// 입력받은 파일 이름에 새로 쓰기
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
		// bookmark 개수 리턴
		return bookmarkArray.size();
	}
	
	public Bookmark getBookmark(int i) {
		// 입력값 i 번째 bookmark 리턴
		return bookmarkArray.get(i);
	}
	
	public void mergeByGroup() {
		// *과제 5* Bookmark[] 를 사용하는 것에서 arrayList 사용으로 변경하였음. 
		// 따라서 몇개의 포인터가 삭제되고, add, remove 메소드로 대체
		
		// 원리 : 0부터 마지막까지 올라가면서, group이름이 있는것을 찾는다.
		// group이름을 찾았다면, 그뒤에 나오는 bookmark 중 그 group이름과 같은 group이름을 가진 bookmark들을, 
		// 새로운 array에 순서를 유지하며 추가한다.
		// 추가한 것은 remove 한다.
		
		// 이렇게 쭉 올라가면서 group이름이 있는것들은 다 새로운 배열에 추가한다.
		// 이때, 그룹이름이 없는 것들은 그냥 순서를 유지하도록 새로운 배열에 추가하면 된다.
		
		ArrayList<Bookmark> newArray = new ArrayList<Bookmark>();
		String groupName = null; // 그룹 이름을 찾으면 여기에 저장되고, 그뒤로 이 이름과 같은 이름을 가진 bookmark를 새로운 배열에 추가한다.
		
		while (numBookmarks() != 0) {
			int tempPointer; // group을 찾은시점부터 그뒤로 탐색할때 사용할 포인터 
			
			if (bookmarkArray.get(0).getGroup().equals("")) {
				// 그룹이름이 없는경우는 순서를 유지 위해 newArray에 그냥 추가한다.
				newArray.add(bookmarkArray.get(0));
				bookmarkArray.remove(0);
			}
			else {
				// 그룹이름이 있다!
				// 그후에 나오는 같은 그룹이름 Bookmark들을 모두 새로운 배열에 추가한다.
				groupName = bookmarkArray.get(0).getGroup();
				tempPointer = 0;
				while (tempPointer < numBookmarks()) {
					if (bookmarkArray.get(tempPointer).getGroup().equals(groupName)){
						// 현재 찾는 groupName 과 같은 group이면, 이것을 새로운 배열에 추가한다.
						// 그리고 기존 배열의 것은 remove
						newArray.add(bookmarkArray.get(tempPointer));
						bookmarkArray.remove(tempPointer);
					}
					else {
						tempPointer++;
					}
				}
			}
		}
		// 새로운 배열에 merge가 완료되었으므로, 기존 참조를 새로운 배열로 옮겨준다.
		bookmarkArray = newArray;
	}
	
	// 과제 6 추가 내용 @@@@
	void addBookmark(Bookmark bm) {
		bookmarkArray.add(bm);
	}
	private void deleteBookmark(Bookmark bm) {
		bookmarkArray.remove(bm);
	}
}
