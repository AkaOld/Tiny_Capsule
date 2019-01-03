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
import model.FriendPreview;

/**
 * Servlet implementation class CapsulePreviewServlet
 */
@WebServlet("/android/friendPreview")
public class FriendPreviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FriendPreviewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		System.out.println("GET FRIEND PREVIEWS");
		ArrayList<FriendPreview> friendPreviews = UserService.getFriendPreviews(username);
		System.out.println("GOT:"+friendPreviews);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(friendPreviews));
		System.out.println(JSONUtil.getJSON(friendPreviews));
		pw.flush();
	}

}
