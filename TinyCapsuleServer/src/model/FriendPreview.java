package model;

public class FriendPreview {
	private String username;
	private String nickname;
	private String icon;
	private int sameCapsule;
	
	public FriendPreview(String username, String nickname, String icon, int signature) {
		this.username = username;
		this.nickname = nickname;
		this.icon = icon;
		this.sameCapsule = signature;
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
	public int getSameCapsule() {
		return sameCapsule;
	}
	public void setSameCapsule(int signature) {
		this.sameCapsule = signature;
	}
	
	
	
}
