package tk.icudi.durandal.web;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@Entity
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

	private String user;
	
	private int tick;
	
	private String code;
	
	@Basic
	private Blob value;
	
	private Date date;

    public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message [key=" + key + ", user=" + user + ", tick=" + tick
				+ ", code=" + code + ", value=" + value + ", date=" + date
				+ "]";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public Blob getValue() {
		return value;
	}

	public void setValue(Blob value) {
		this.value = value;
	}



}
