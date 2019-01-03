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
import model.CapsuleContent;
import model.CapsulePreview;

/**
 * Servlet implementation class CapsulePreviewServlet
 */
@WebServlet("/android/capsuleContents")
public class CapsuleContentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CapsuleContentsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		int capsuleId = Integer.parseInt(request.getParameter("capsuleId"));
		System.out.println("GET CAPSULE CONTENTS");
		ArrayList<CapsuleContent> capsuleContents = CapsuleService.getCapsuleContents(capsuleId);
		System.out.println("GOT:"+capsuleContents);
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSON(capsuleContents));
		System.out.println(JSONUtil.getJSON(capsuleContents));
		pw.flush();
	}

}
