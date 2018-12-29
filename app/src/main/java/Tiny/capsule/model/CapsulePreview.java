package Tiny.capsule.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CapsulePreview extends RealmObject{
	private int id;
	private String name;
	private String host;
	private boolean isPublic;
	private String background;
	private long lastPost;
	private String lastContet;
	private RealmList<String> icons;

	public CapsulePreview(){}
	public CapsulePreview(int id, String name, String host, boolean isPublic, String background, long lastPost,
			String lastContet, RealmList<String> icons) {
		super();
		this.id = id;
		this.name = name;
		this.host = host;
		this.isPublic = isPublic;
		this.background = background;
		this.lastPost = lastPost;
		this.lastContet = lastContet;
		this.icons = icons;
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
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public long getLastPost() {
		return lastPost;
	}
	public void setLastPost(long lastPost) {
		this.lastPost = lastPost;
	}
	public String getLastContet() {
		return lastContet;
	}
	public void setLastContet(String lastContet) {
		this.lastContet = lastContet;
	}
	public RealmList<String> getIcons() {
		return icons;
	}
	public void setIcons(RealmList<String> icons) {
		this.icons = icons;
	}
	
}
