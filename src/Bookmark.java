import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bookmark {
	private String name;
	private LocalDateTime time; // essential
	private String url;  // essential
	private String group;
	private String memo;
	
	Bookmark(String name, LocalDateTime time, String url, String group, String memo){
		// constructor with all parameters, some can be ""
		this.name = name;
		this.time = time;
		this.url = url;
		this.group = group;
		this.memo = memo;
	}
	
	Bookmark(String url){
		// constructor with url, time = now
		this("", LocalDateTime.now(), url, "", "");
	}
	
	public void print() {
		System.out.println(getStringFormat());
	}
	
	public String getStringFormat() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		return (name + " ; " + time.format(formatter) + " ; " + url + " ; " + group + " ; " + memo);
	}
}
