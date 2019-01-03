package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataaccess.CapsuleService;
import dataaccess.JSONUtil;
import dataaccess.UserService;
import model.CapsulePreview;
import model.FriendRequest;

/**
 * Servlet implementation class CapsulePreviewServlet
 */
@WebServlet("/android/friendRequest")
public class FriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FriendRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		System.out.println("GET FRIEND REQUEST");
		ArrayList<FriendRequest> friendRequests = UserService.getFriendRequests(username);
		System.out.println("GOT:"+friendRequests);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(friendRequests));
		System.out.println(JSONUtil.getJSON(friendRequests));
		pw.flush();
	}

}
