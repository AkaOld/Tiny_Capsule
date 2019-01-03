package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import model.CapsulePreview;
import model.CapsuleRequest;
import model.FriendPreview;
import model.FriendRequest;
import model.User;
import model.UserInfo;

public class UserService {
	private static Connection connection;
	
	public static int SUCCEED = 1;
	public static int REGISTER_OCCUPIED = -1;
	public static int register(String loginName,String password,String nickname,String thirdParty) {
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM user WHERE login_name=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return REGISTER_OCCUPIED;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String username = generateUsername();
		sql = "INSERT INTO user(login_name,password,username,nickname,third_party) values(?,?,?,?,?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			statement.setString(2, password);
			statement.setString(3, username);
			statement.setString(4, nickname);
			statement.setString(5, thirdParty);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return SUCCEED;
	}
	
	public static String getUsername(String loginName) {
		String sql = "SELECT * FROM user WHERE login_name=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, loginName);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNickname(String username) {
		String sql = "SELECT * FROM user WHERE username=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				return rs.getString("nickname");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int login(String username,String password) {
		final int WRONG_PASSWORD = -1;
		final int NO_USER = -2;
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM user WHERE login_name=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				String pass = rs.getString("password");
				if(password.equals(pass)) {
					return SUCCEED;
				}
				else {
					return WRONG_PASSWORD;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return NO_USER;
	}
	
	public static int changeNickname(String username,String nickname) {
		connection = DBUtil.getConnection();
		String sql = "UPDATE user SET nickname='?' WHERE username=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(2, nickname);
			statement.setString(2, username);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return SUCCEED;
	}

	public static ArrayList<FriendPreview> getFriendPreviews(String username) {
		final int WRONG_PASSWORD = -1;
		final int NO_USER = -2;
		connection = DBUtil.getConnection();
		ArrayList<String> friends = new ArrayList();
		String sql = "SELECT username_1 FROM friend_list WHERE username_2=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				String friend = rs.getString("username_1");
				if(friend != null) {
					friends.add(friend);
				}
			}
			
			sql = "SELECT username_2 FROM friend_list WHERE username_1=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			rs = statement.executeQuery();
			while(rs.next()) {
				String friend = rs.getString("username_2");
				if(friend != null) {
					friends.add(friend);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<FriendPreview> friendPreviews = new ArrayList();
		for (String friend :friends) {
			sql = "SELECT * FROM user WHERE username=?";
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, friend);
				
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					FriendPreview friendPreview = new FriendPreview(rs.getString("username"), rs.getString("nickname"),rs.getString("icon"), getSameCapsuleCount(username, rs.getString("username")));
					friendPreviews.add(friendPreview);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return friendPreviews;
	}
	
	public static int getSameCapsuleCount(String username1,String username2) {
		int num = 0;
		ArrayList<CapsulePreview> capsules1 = CapsuleService.getCapsulePreviews(username1);
		ArrayList<CapsulePreview> capsules2 = CapsuleService.getCapsulePreviews(username2);
		for(CapsulePreview capsule1:capsules1) {
			for(CapsulePreview capsule2:capsules2) {
				if(capsule1.getId()==capsule2.getId()) {
					num++;
					break;
				}
			}
		}
		return num;
	}
	
	
	
	public static UserInfo getUserInfo(String username) {
		connection = DBUtil.getConnection();
		String sql = "SELECT * FROM user WHERE username=?";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			ArrayList<CapsulePreview> capsulePreviews = CapsuleService.getCapsulePreviews(username);
			ArrayList<String> capsuleNames = new ArrayList();
			ArrayList<Integer> usercounts = new ArrayList();
			ArrayList<Integer> capsuleIds = new ArrayList();
			for(CapsulePreview capsulePreview:capsulePreviews) {
				capsuleNames.add(capsulePreview.getName());
				usercounts.add(capsulePreview.getIcons().size());
				capsuleIds.add(capsulePreview.getId());
			}
			if(rs.next()) {
				UserInfo userInfo = new UserInfo(rs.getString("username"), rs.getString("nickname"),rs.getString("icon"), rs.getString("signature"),capsuleIds,capsuleNames,usercounts);
				return userInfo;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<User> searchForUser(String username) {
		connection = DBUtil.getConnection();
		ArrayList<User> results = new ArrayList();
		String sql = "SELECT * FROM user WHERE username=%?%";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				User user =new User();
				results.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static void confirmFriendRequest(String username,String username2) {
		connection = DBUtil.getConnection();
		ArrayList<User> results = new ArrayList();
		String sql = "Insert into friend_list values(?,?)";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, username2);
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int sendFriendRequest(String request,String receive) {
		connection = DBUtil.getConnection();
		String sql = "insert into `tiny_capsule`.`friend_request` ( `request_username`,`receive_username`, `request_time`) values ( ?, ?, ?)";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, request);
			statement.setString(2, receive);
			statement.setLong(3, System.currentTimeMillis());
			
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static ArrayList<FriendRequest> getFriendRequests(String username) {
		connection = DBUtil.getConnection();
		String sql = "select * from `tiny_capsule`.`friend_request`  where request_username = ? or receive_username = ?";
		PreparedStatement statement = null;
		ArrayList<FriendRequest> friendRequests = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, username);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				FriendRequest request = new FriendRequest(rs.getString("request_username"), rs.getString("receive_username"),UserService.getNickname(rs.getString("request_username")),UserService.getNickname(rs.getString("receive_username")), rs.getLong("request_time"), rs.getLong("confirm_time"));
				friendRequests.add(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friendRequests;
	}
	
	public static ArrayList<CapsuleRequest> getCapsuleRequests(String username) {
		connection = DBUtil.getConnection();
		String sql = "select * from `tiny_capsule`.`capsule_request`  where request_username = ? or receive_username = ?";
		PreparedStatement statement = null;
		ArrayList<CapsuleRequest> capsuleRequests = new ArrayList();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, username);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				CapsuleRequest request = new CapsuleRequest(rs.getString("request_username"), rs.getString("receive_username"),UserService.getNickname(rs.getString("request_username")),UserService.getNickname(rs.getString("receive_username")),rs.getInt("capsule_id"), 
						CapsuleService.getCapsuleName(rs.getInt("capsule_id")),rs.getLong("request_time"), rs.getLong("confirm_time"));
				capsuleRequests.add(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return capsuleRequests;
	}
	
	public static String generateUsername() {
		connection = DBUtil.getConnection();
		while(true) {
			String username = "";
			username += RandomStringUtils.randomAlphanumeric(5);
			username += "-";
			username += RandomStringUtils.randomAlphanumeric(5);
			username += "-";
			username += RandomStringUtils.randomAlphanumeric(5);
			
			String sql = "select * from `tiny_capsule`.`user`  where username = ?";
			PreparedStatement statement = null;
			ArrayList<CapsuleRequest> capsuleRequests = new ArrayList();
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, username);
				
				ResultSet rs = statement.executeQuery();
				
				if(!rs.next()) {
					return username;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		UserService service = new UserService();
//		for(int i =0;i<10;i++) {
//			service.register(RandomStringUtils.randomAlphanumeric((int) (Math.random()*10)), RandomStringUtils.randomAlphanumeric((int) (Math.random()*10)));
//		}
//		System.out.println(service.getFriendRequests("admin"));
//		System.out.println(JSONUtil.getJSON(service.getFriendRequests("admin")));
//		System.out.println(JSONUtil.getJSON(getFriendPreviews("admin")));

//		UserService.register(RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6), "王老一", null);
//		UserService.register(RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6), "王老二", null);
//		UserService.register(RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6), "王老三", null);
//		UserService.register(RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6), "王老四", null);
//		UserService.register(RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6), "王老五", null);
	}
}
