package Tiny.capsule.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CapsuleContent extends RealmObject{
	private int capsuleId;
	private String username;
	private String nickname;
	private long timeStamp;
	private String content;
	private String location;
	RealmList<String> resourceUrl;
	
	
	public CapsuleContent(int capsuleId, String username,String nickname, long timeStamp, String content, String location,
			RealmList<String> resourceUrl) {
		this.capsuleId = capsuleId;
		this.username = username;
		this.nickname = nickname;
		this.timeStamp = timeStamp;
		this.content = content;
		this.location = location;
		this.resourceUrl = resourceUrl;
	}
	public CapsuleContent() {}
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
	public RealmList<String> getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(RealmList<String> resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
