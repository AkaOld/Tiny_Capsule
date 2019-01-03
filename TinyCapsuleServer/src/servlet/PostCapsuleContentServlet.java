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
import model.CapsuleContent;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@WebServlet("/android/postCapsuleContent")
public class PostCapsuleContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PostCapsuleContentServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String capsuleContentJSON = request.getParameter("capsuleContent");
		CapsuleContent capsuleContent = CapsuleContent.getCapsuleContent(capsuleContentJSON);
		System.out.println(JSONUtil.getJSON(JSONUtil.getObject(capsuleContentJSON)));
		int status = CapsuleService.postCapsuleContent(capsuleContent.getUsername(), capsuleContent.getCapsuleId(),capsuleContent.getContent(),capsuleContent.getLocation(),capsuleContent.getTimeStamp());
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSONStatus(status));
		pw.flush();
	}

}
