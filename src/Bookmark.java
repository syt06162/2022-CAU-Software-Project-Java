import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bookmark {
	private String name;
	private LocalDateTime time; // �ʼ����
	private String url;  // �ʼ����
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
		// ������ �ۼ��� �����ڸ� �� �̿�
		this("", LocalDateTime.now(), url, "", "");
	}
	
	public void print() {
		System.out.println(getStringFormat());
	}
	
	public String getStringFormat() {
		// Bookmark�� 5���� �ʵ带, String���� ���� ���� �����ϴ� �޼ҵ�
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		return (name + "," + time.format(formatter) + "," + url + "," + group + "," + memo);
	}
	
	public String getGroup() {
		return group;
	} 
}
