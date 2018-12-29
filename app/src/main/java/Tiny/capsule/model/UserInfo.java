package Tiny.capsule.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class UserInfo extends RealmObject{
	private String username;
	private String nickname;
	private String icon;
	private String signature;
	private RealmList<Integer> capsuleId;
	private RealmList<String> capsuleNames;
	private RealmList<Integer> userCount;
	
	public UserInfo(){

	}

	public UserInfo(String username, String nickname, String icon, String signature, RealmList<Integer> capsuleId,
			RealmList<String> capsuleNames, RealmList<Integer> userCount) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.icon = icon;
		this.signature = signature;
		this.capsuleId = capsuleId;
		this.capsuleNames = capsuleNames;
		this.userCount = userCount;
	}
	
	
	public RealmList<Integer> getCapsuleId() {
		return capsuleId;
	}


	public void setCapsuleId(RealmList<Integer> capsuleId) {
		this.capsuleId = capsuleId;
	}


	public RealmList<String> getCapsuleNames() {
		return capsuleNames;
	}


	public void setCapsuleNames(RealmList<String> capsuleNames) {
		this.capsuleNames = capsuleNames;
	}


	public RealmList<Integer> getUserCount() {
		return userCount;
	}


	public void setUserCount(RealmList<Integer> userCount) {
		this.userCount = userCount;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
