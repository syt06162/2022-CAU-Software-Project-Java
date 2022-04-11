import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bookmark {
	private String name;
	private String time; // essential
	private String url;  // essential
	private String group;
	private String memo;
	
	Bookmark(String name, String time, String url, String group, String memo){
		// constructor with all parameters, some can be ""
		this.name = name;
		this.time = time;
		this.url = url;
		this.group = group;
		this.memo = memo;
	}
	
	Bookmark(String url){
		// constructor with url, time = now
		this.name = "";
		LocalDateTime dateAndTime =LocalDateTime.now();
		this.time = dateAndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"));
		this.url = url;
		this.group = "";
		this.memo = "";
	}
	
	public void print() {
		System.out.println(name + " ; " + time + " ; " + url + " ; " + group + " ; " + memo);
	}
}
