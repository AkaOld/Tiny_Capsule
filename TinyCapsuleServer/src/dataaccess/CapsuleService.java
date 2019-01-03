package dataaccess;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;

import model.Capsule;
import model.CapsuleContent;
import model.CapsuleInfo;
import model.CapsulePreview;
import model.UserInfo;
import net.sf.json.JSON;


public class CapsuleService {
	private static Connection connection;
	public static int createCapsule(String username,String name,Boolean isPublic) {
		connection = DBUtil.getConnection();
		String sql = "INSERT INTO capsule(name,host,is_public) values(?, ?,?)";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, username);
			statement.setInt(3, isPublic?1:0);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int id = 0;
		try {
			statement = connection.prepareStatement("SELECT MAX(id) from capsule");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		sql = "INSERT INTO capsule_user(username,capsule_id,join_time) values(?,?,?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setInt(2, id);
			statement.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
			
			statement.execute();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return 1;
	}
	
	public static ArrayList<CapsulePreview> getCapsulePreviews(String username) {
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM capsule_user left join capsule on capsule.id = capsule_user.capsule_id WHERE username =?";
		PreparedStatement statement;
		ArrayList<CapsulePreview> capsules = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int capsuleId = rs.getInt("id");
				ArrayList<String> usernames = getUsersInCapsule(capsuleId);
				ArrayList<CapsuleContent> contents = getCapsuleContents(capsuleId);
				CapsuleInfo capsuleInfo = getCapsuleInfo(capsuleId);
				System.out.println(capsuleInfo.getBackground());
				if(contents.size()>0) {
					CapsuleContent content = contents.get(0);
					CapsulePreview capsule = new CapsulePreview(capsuleId, rs.getString("name"), rs.getString("host"), rs.getInt("is_public")==1?true:false,capsuleInfo.getBackground(), content.getTimeStamp(), content.getContent(),  usernames);
					capsules.add(capsule);
				}
				else {
					CapsulePreview capsule = new CapsulePreview(capsuleId, rs.getString("name"), rs.getString("host"), rs.getInt("is_public")==1?true:false,capsuleInfo.getBackground(), 0, null,  usernames);
					capsules.add(capsule);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return capsules;
	}
	
	public static ArrayList<String> getUsersInCapsule(int id){
		connection = DBUtil.getConnection();
		String sql = "select * from `tiny_capsule`.`capsule_user` where capsule_id = ?";
		PreparedStatement statement;
		ArrayList<String> usernames = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				usernames.add(rs.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usernames;
	}
	
	
	
	public static int  postCapsuleContent(String username,int capsuleId,String content,String location,long timestamp) {
		connection = DBUtil.getConnection();
		String sql = "insert into `tiny_capsule`.`capsule_content` ( `username`, `capsule_id`, `time`,`content`,`location` ) values(?,?,?,?,?)";
		PreparedStatement statement;
		ArrayList<Capsule> capsules = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setInt(2, capsuleId);
			statement.setLong(3, timestamp);
			statement.setString(4, content);
			statement.setString(5, location);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int contentId = 0;
		sql = "select * from `tiny_capsule`.`capsule_content` order by id desc";
		try {
			statement = connection.prepareStatement(sql);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				contentId = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = "insert into `tiny_capsule`.`content_resource` (`content_id`, `sequence`,`url` ) values(?,1,'picture')";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, contentId);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static int sendCapsuleRequest(String request,String receive,int capsuleId) {
		connection = DBUtil.getConnection();
		String sql = "insert into `tiny_capsule`.`capsule_request` (   `request_username`,`receive_username`,`capsule_id`,`request_time`)values ( ?, ?, ?, ?)";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, request);
			statement.setString(2, receive);
			statement.setInt(3, capsuleId);
			statement.setLong(4, System.currentTimeMillis());
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static ArrayList<CapsuleContent> getCapsuleContents(int capsuleId) {
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM capsule_content WHERE capsule_id =? order by time desc";
		PreparedStatement statement;
		ArrayList<CapsuleContent> capsuleContents = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, capsuleId);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("capsule_id");
				ArrayList<String> resources = getContentResourceUrl(rs.getInt("id"));
				CapsuleContent capsuleContent = new CapsuleContent(id,rs.getString("username"),UserService.getNickname(rs.getString("username")), rs.getLong("time"), rs.getString("content"),rs.getString("location"),resources);
				capsuleContents.add(capsuleContent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return capsuleContents;
	}
	
	public static ArrayList<String> getContentResourceUrl(int capsuleId){
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM content_resource WHERE content_id =?";
		PreparedStatement statement;
		ArrayList<String> resources = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, capsuleId);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				resources.add(rs.getString("url"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resources;
	}
	
	
	public static CapsuleInfo getCapsuleInfo(int capsuleId) {
		connection = DBUtil.getConnection();
		String sql = "select * from `tiny_capsule`.`capsule` WHERE id =?";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, capsuleId);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				ArrayList<String> users = new ArrayList();
				sql = "select * from `tiny_capsule`.`capsule_user` WHERE capsule_id =?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, capsuleId);
				ResultSet rs2 = statement.executeQuery();
				while(rs2.next()) {
					users.add(rs2.getString("username"));
				}
				System.out.println(rs.getString("background"));
				CapsuleInfo info = new CapsuleInfo(capsuleId,rs.getString("background"),rs.getString("host"), rs.getInt("is_public")==1?true:false, users);
				return info;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getCapsuleName(int capsuleId) {
		connection = DBUtil.getConnection();
		String sql = "select * from `tiny_capsule`.`capsule` WHERE id =?";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, capsuleId);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) { 
		CapsuleService service = new CapsuleService();
		System.out.println(JSONUtil.getJSON(service.getCapsulePreviews("admin")));
	}
}
