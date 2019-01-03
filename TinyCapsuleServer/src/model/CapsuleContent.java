package model;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public class CapsuleContent {
	private int capsuleId;
	private String username;
	private String nickname;
	private long timeStamp;
	private String content;
	private String location;
	ArrayList<String> resourceUrl;
	
	
	
	public CapsuleContent(int capsuleId, String username, String nickname, long timeStamp, String content,
			String location, ArrayList<String> resourceUrl) {
		super();
		this.capsuleId = capsuleId;
		this.username = username;
		this.nickname = nickname;
		this.timeStamp = timeStamp;
		this.content = content;
		this.location = location;
		this.resourceUrl = resourceUrl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public CapsuleContent() {}
	public static CapsuleContent getCapsuleContent(String json) {
		return (CapsuleContent) JSONObject.toBean(JSONObject.fromObject(json),CapsuleContent.class);
	}
	public int getCapsuleId() {
		return capsuleId;
	}

	public void setCapsuleId(int capsuleId) {
		this.capsuleId = capsuleId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ArrayList<String> getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(ArrayList<String> resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	
	
}
