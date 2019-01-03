package model;

public class FriendRequest {
	private String requestUsername;
	private String receiveUsername;
	private String requestNickname;
	private String receiveNickname; 
	private long requestTime;
	private long confirmTime;
	
	


	public FriendRequest(String requestUsername, String receiveUsername, String requestNickname, String receiveNickname,
			long requestTime, long confirmTime) {
		super();
		this.requestUsername = requestUsername;
		this.receiveUsername = receiveUsername;
		this.requestNickname = requestNickname;
		this.receiveNickname = receiveNickname;
		this.requestTime = requestTime;
		this.confirmTime = confirmTime;
	}


	public String getRequestNickname() {
		return requestNickname;
	}


	public void setRequestNickname(String requestNickname) {
		this.requestNickname = requestNickname;
	}


	public String getReceiveNickname() {
		return receiveNickname;
	}


	public void setReceiveNickname(String receiveNickname) {
		this.receiveNickname = receiveNickname;
	}


	public String getRequestUsername() {
		return requestUsername;
	}


	public void setRequestUsername(String requestUsername) {
		this.requestUsername = requestUsername;
	}


	public String getReceiveUsername() {
		return receiveUsername;
	}


	public void setReceiveUsername(String receiveUsername) {
		this.receiveUsername = receiveUsername;
	}


	public long getRequestTime() {
		return requestTime;
	}


	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}


	public long getConfirmTime() {
		return confirmTime;
	}


	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}
	
	
	
}
