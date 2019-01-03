package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataaccess.CapsuleService;
import dataaccess.JSONUtil;
import dataaccess.UserService;

@WebServlet("/android/capsuleInvite")
public class CapsuleInviteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CapsuleInviteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String requestUser = request.getParameter("request");
		String receiveUser = request.getParameter( "receive");
		int capsuleId = Integer.parseInt(request.getParameter( "capsuleId"));
		
		System.out.println("CAPSULE INVITE");
		int status = CapsuleService.sendCapsuleRequest(requestUser, receiveUser, capsuleId);
		System.out.println("CAPSULE INVITE STATUS:"+status);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSONStatus(status));
		pw.flush();
	}

}
