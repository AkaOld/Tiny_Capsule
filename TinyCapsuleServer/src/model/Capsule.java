package model;

import net.sf.json.JSONObject;

public class Capsule {
	private int id;
	private String name;
	private String host;
	private boolean isPublic;
	
	public Capsule() {}
	public Capsule(int id, String name, String host, boolean isPublic) {
		this.id = id;
		this.name = name;
		this.host = host;
		this.isPublic = isPublic;
	}
	public static Capsule getCapsule(String json) {
		return (Capsule) JSONObject.toBean(JSONObject.fromObject(json),Capsule.class);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
}
