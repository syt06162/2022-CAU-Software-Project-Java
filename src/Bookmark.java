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
		// 위에서 작성한 생성자를 또 이용
		this("", LocalDateTime.now(), url, "", "");
	}
	
	public void print() {
		System.out.println(getStringFormat());
	}
	
	public String getStringFormat() {
		// Bookmark의 5가지 필드를, String으로 보기 좋게 리턴하는 메소드
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		return (name + "," + time.format(formatter) + "," + url + "," + group + "," + memo);
	}
	
	public String getGroup() {
		return group;
	} 
}
