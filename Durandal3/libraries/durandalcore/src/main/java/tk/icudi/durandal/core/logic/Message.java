package tk.icudi.durandal.core.logic;

import java.io.IOException;
import java.util.StringTokenizer;

public class Message {

	private long id;
	private int tick;
	private String user;
	private String command;
	private String value;
	private String date;
	private String ago;
	
	private transient ShortMessage msg;

	public Message() {
		
	}
	
	public Message(String line) {
		
		StringTokenizer tokenizer = new StringTokenizer(line, "\t");
		
		this.id = Long.valueOf(tokenizer.nextToken());
		this.tick = Integer.valueOf(tokenizer.nextToken());
		this.user = tokenizer.nextToken();
		this.command = tokenizer.nextToken();
		this.date = tokenizer.nextToken();
		this.ago = tokenizer.nextToken();
		this.value = tokenizer.nextToken();
	}

	@Override
	public String toString() {
		return "Message [tick=" + tick + ", user=" + user
				+ ", command=" + command + ", value=" + value + "]";
	}

	public int getTick() {
		return tick;
	}
	
	public void setTick(int tick) {
		this.tick = tick;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAgo() {
		return ago;
	}

	public void setAgo(String ago) {
		this.ago = ago;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ShortMessage getMsg() {
		return msg;
	}

	public void setMsg(ShortMessage msg) throws IOException {
		this.msg = msg;
		this.value = Serializer.objectToString(msg);
	}

}
