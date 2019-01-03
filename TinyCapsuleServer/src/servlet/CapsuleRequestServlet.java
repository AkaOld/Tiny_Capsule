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
import model.CapsuleRequest;
import model.CapsuleRequest;

/**
 * Servlet implementation class CapsulePreviewServlet
 */
@WebServlet("/android/capsuleRequest")
public class CapsuleRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CapsuleRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		System.out.println("GET CAPSULE REQUEST");
		ArrayList<CapsuleRequest> capsuleRequests = UserService.getCapsuleRequests(username);
		System.out.println("GOT:"+capsuleRequests);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(capsuleRequests));
		System.out.println(JSONUtil.getJSON(capsuleRequests));
		pw.flush();
	}

}
