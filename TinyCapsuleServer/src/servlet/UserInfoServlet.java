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
import model.UserInfo;

@WebServlet("/android/userInfo")
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserInfoServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		System.out.println("USER INFO");
		UserInfo userInfo = UserService.getUserInfo(username);
		System.out.println("USER INFO:"+userInfo);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(userInfo));
		pw.flush();
	}

}
