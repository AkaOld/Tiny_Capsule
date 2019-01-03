package model;

import java.util.ArrayList;

public class CapsuleInfo {
	private int capsuleId;
	private String background;
	private String host;
	private boolean isPublic;
	private ArrayList<String> users;
	public int getCapsuleId() {
		return capsuleId;
	}
	public void setCapsuleId(int capsuleId) {
		this.capsuleId = capsuleId;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public ArrayList<String> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
	public CapsuleInfo(int capsuleId, String background, String host, boolean isPublic, ArrayList<String> users) {
		super();
		this.capsuleId = capsuleId;
		this.background = background;
		this.host = host;
		this.isPublic = isPublic;
		this.users = users;
	}
	
}