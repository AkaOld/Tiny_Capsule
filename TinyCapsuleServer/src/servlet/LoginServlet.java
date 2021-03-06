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

@WebServlet("/android/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		String password = request.getParameter("password");
		System.out.println("LOGIN");
		int loginStatus = UserService.login(username, password);
		System.out.println("LOGIN STATUS:"+loginStatus);
		PrintWriter pw = response.getWriter();
		pw.write("{ \"status\":\""+loginStatus+"\",\"username\":\""+UserService.getUsername(username)+"\"}");
		pw.flush();
	}

}
