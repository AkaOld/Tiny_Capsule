package model;

import java.util.ArrayList;

public class UserInfo {
	private String username;
	private String nickname;
	private String icon;
	private String signature;
	private ArrayList<Integer> capsuleId;
	private ArrayList<String> capsuleNames;
	private ArrayList<Integer> userCount;
	
	
	public UserInfo(String username, String nickname, String icon, String signature, ArrayList<Integer> capsuleId,
			ArrayList<String> capsuleNames, ArrayList<Integer> userCount) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.icon = icon;
		this.signature = signature;
		this.capsuleId = capsuleId;
		this.capsuleNames = capsuleNames;
		this.userCount = userCount;
	}
	
	
	public ArrayList<Integer> getCapsuleId() {
		return capsuleId;
	}


	public void setCapsuleId(ArrayList<Integer> capsuleId) {
		this.capsuleId = capsuleId;
	}


	public ArrayList<String> getCapsuleNames() {
		return capsuleNames;
	}


	public void setCapsuleNames(ArrayList<String> capsuleNames) {
		this.capsuleNames = capsuleNames;
	}


	public ArrayList<Integer> getUserCount() {
		return userCount;
	}


	public void setUserCount(ArrayList<Integer> userCount) {
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
