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
		// bookmarkFileName 을 입력받아, 줄 별로 북마크 정보를 파싱해 북마크를 추가하는 메소드
		
		Scanner input = null;
		String line = "";
		String[] parsed;
		
		int lineCount = 1; // 첫번째 줄부터 읽음
		int addCount = 0; // 정상 add 개수
		int errCount = 0; // 에러 라인 개수
		
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
					
					if (parsed.length!=5) {
						// 에러 라인: 파싱된 항목이 5개가 아님. 
						errCount++;
						System.out.println(lineCount + "번째 줄 에러: 파싱된 항목이 5개가 아닙니다.");
					}
					else if (parsed[1]=="" && parsed[2]=="") {
						// 에러 라인: 필수요소(time, url) 둘다 없음
						errCount++;						
						System.out.println(lineCount + "번째 줄 에러: 필수요소(time, url) 둘다 없습니다.");
					}
					else if (parsed[1]=="") {
						// 에러 라인: 필수요소(time) 없음
						errCount++;						
						System.out.println(lineCount + "번째 줄 에러: 필수요소(time)이 없습니다.");
					}
					else if (parsed[2]=="") {
						// 에러 라인: 필수요소(url) 없음
						errCount++;						
						System.out.println(lineCount + "번째 줄 에러: 필수요소(url)이 없습니다.");
					}
					else {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
							LocalDateTime dateAndTime = LocalDateTime.parse(parsed[1], formatter);
							
							// 정상 라인: bookmark 생성 및 추가
							bookmarkArray[bookmarkCount++] = new Bookmark(parsed[0], dateAndTime, parsed[2], parsed[3], parsed[4]);
							addCount++;
							
						} catch(DateTimeParseException e) {
							// 에러 라인: 시간 형식이 잘못됨.
							errCount++;					
							System.out.println(lineCount + "번째 줄 에러: 시간 형식이 잘못되었습니다.");
							}
					}
				}
				
				lineCount++;
			}
			
			
			System.out.println("add: " + addCount + "개, error: " + errCount +"개" );
			
			
		}
		catch(FileNotFoundException e) {
			// e.printStackTrace();
			System.out.println("에러: 지정된 파일을 찾을 수 없습니다.");
		}
	}
	
	
	void saveBookmarks(String bookmarkFileName) {
		// BookmarkList에 저장되어 있는 모든 Bookmark들을, 형식에 맞게 파일에 저장
		// 입력받은 파일 이름에 새로 쓰기
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
