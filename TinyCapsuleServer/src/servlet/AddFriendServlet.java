package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataaccess.JSONUtil;
import dataaccess.UserService;

@WebServlet("/android/addFriend")
public class AddFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddFriendServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestUser = request.getParameter("request");
		String receiveUser = request.getParameter( "receive");
		
		System.out.println("ADD FRIEND");
		int status = UserService.sendFriendRequest(requestUser, receiveUser);
		System.out.println("ADD FRIEND STATUS:"+status);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSONStatus(status));
		pw.flush();
	}

}
