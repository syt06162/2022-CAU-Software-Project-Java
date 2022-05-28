import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bookmark {
	private String name;
	private LocalDateTime time; // 필수요소
	private String url;  // 필수요소
	private String group;
	private String memo;
	
	Bookmark(String name, LocalDateTime time, String url, String group, String memo){
		this.name = name;
		this.time = time;
		this.url = url;
		this.group = group;
		this.memo = memo;
	}
	
	Bookmark(String url){
		this("", LocalDateTime.now(), url, "", "");
	}
	
	// 과제 6 - ADD 시 생성하는 생성자. time 제외 나머지는 사용자 입력, time은 현재 시각
	Bookmark(String name, String url, String group, String memo){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		LocalDateTime time = LocalDateTime.now()
				.withNano(0).withSecond(0);
		
		this.name = name;
		this.time = time;
		this.url = url;
		this.group = group;
		this.memo = memo;
	}
	
	public void print() {
		System.out.println(getStringFormat());
	}
	
	public String getStringFormat() {
		// Bookmark의 5가지 필드를, String으로 보기 좋게 리턴하는 메소드
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		return (name + "," + time.format(formatter) + "," + url + "," + group + "," + memo);
	}
	
	public String getName() {
		return name;
	} 
	public String getTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		return time.format(formatter);
	} 
	public String getUrl() {
		return url;
	} 
	public String getGroup() {
		return group;
	} 
	public String getMemo() {
		return memo;
	} 
}

