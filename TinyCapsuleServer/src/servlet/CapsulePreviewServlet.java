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

/**
 * Servlet implementation class CapsulePreviewServlet
 */
@WebServlet("/android/capsulePreview")
public class CapsulePreviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CapsulePreviewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter( "username");
		System.out.println("GET CAPSULE PREVIEWS");
		ArrayList<CapsulePreview> capsulePreviews = CapsuleService.getCapsulePreviews(username);
		System.out.println("GOT:"+capsulePreviews);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(capsulePreviews));
		System.out.println(JSONUtil.getJSON(capsulePreviews));
		pw.flush();
	}

}
