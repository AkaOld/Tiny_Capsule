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

@WebServlet("/android/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RegisterServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String loginName = request.getParameter( "loginName");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		System.out.println(loginName+":"+password+"nickname");
		System.out.println("REGISTER");
		int registerStatus = UserService.register(loginName, password, nickname, null);
		String username = UserService.getUsername(loginName);
		System.out.println("REGISTER STATUS:"+registerStatus);
		PrintWriter pw = response.getWriter();
		pw.write("{ \"status\":\""+registerStatus+"\",\"username\":\""+username+"\"}");
		pw.flush();
	}

}
