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
	private int bookmarkCount; // bookmark 개수
	private final int MAX_SIZE = 100;
	
	BookmarkList(String bookmarkFileName){
		bookmarkArray = new Bookmark[MAX_SIZE];
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
							bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]);
							
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
		// bookmark 개수 리턴
		return bookmarkCount;
	}
	
	public Bookmark getBookmark(int i) {
		// 입력값 i 번째 bookmark 리턴
		return bookmarkArray[i];
	}
	
	public void mergeByGroup() {
		// 원리 : 0부터 마지막까지 올라가면서, group이름이 있는것을 찾는다.
		// group이름을 찾았다면, 그뒤에 나오는 bookmark 중 그 group이름과 같은 group이름을 가진 bookmark들을, 
		// 새로운 array에 순서를 유지하며 추가한다.
		// 추가한 것은 참조를 해제해서, 다음번 탐색때 또 하지 않게 한다.
		
		// 이렇게 쭉 올라가면서 group이름이 있는것들은 다 새로운 배열에 추가한다.
		// 마지막으로 다시 0번부터 탐색해 group이름이 없는것들을 새로운 배열 뒷부분에 추가해준다.
		
		Bookmark[] newArray = new Bookmark[MAX_SIZE];
		int oldPointer = 0; // 기존 배열의, 현재 탐색중인 인덱스를 가리키는 값
		int newPointer = 0; // 새로운 배열의, 추가되어야 하는 곳의 인덱스를 가리키는 값
		String groupName = null; // 그룹 이름을 찾으면 여기에 저장되고, 그뒤로 이 이름과 같은 이름을 가진 bookmark를 새로운 배열에 추가한다.
		
		while (oldPointer < bookmarkCount) {
			int tempPointer; // group을 찾은시점부터 그뒤로 탐색할때 사용할 포인터 

			if (bookmarkArray[oldPointer] != null && !bookmarkArray[oldPointer].getGroup().equals("")) {
				groupName = bookmarkArray[oldPointer].getGroup();
				tempPointer = oldPointer; // oldPointer 값부터 끝까지 탐색한다.
				while (tempPointer < bookmarkCount) {
					if (bookmarkArray[tempPointer] != null && bookmarkArray[tempPointer].getGroup().equals(groupName)){
						// 현재 찾는 groupName 과 같은 group이면, 이것을 새로운 배열에 추가한다.
						// 그리고 기존 배열의 참조는 null로 만들어, 다음번 탐색에서 또 걸리지 않게 한다.
						newArray[newPointer] = bookmarkArray[tempPointer];
						bookmarkArray[tempPointer] = null;
						newPointer++;
					}
					tempPointer++;
				}
				
			}
			oldPointer++;
		}
		// 여기까지 되면 그룹이름이 있는 것들은 모두 옮겨졌다.
		
		oldPointer = 0;
		// 이제 그룹이름이 없는 것들을 앞에서부터 하나하나 새로운 배열로 옮겨준다.
		while (oldPointer < bookmarkCount) {
			if (bookmarkArray[oldPointer] != null && bookmarkArray[oldPointer].getGroup().equals("")) {
				newArray[newPointer] = bookmarkArray[oldPointer];
				bookmarkArray[oldPointer] = null;
				newPointer++;
			}
			oldPointer++;
		}
		
		// 새로운 배열에 merge가 완료되었으므로, 기존 참조를 새로운 배열로 옮겨준다.
		bookmarkArray = newArray;
		
	}
}
