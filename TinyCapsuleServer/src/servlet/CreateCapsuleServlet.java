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
import model.Capsule;
import model.CapsuleContent;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@WebServlet("/android/createCapsule")
public class CreateCapsuleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateCapsuleServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String capsuleJSON = request.getParameter("capsule");
		Capsule capsule = Capsule.getCapsule(capsuleJSON);
		int status = CapsuleService.createCapsule(capsule.getHost(), capsule.getName(), capsule.isPublic());
		PrintWriter pw = response.getWriter();
		pw.write(JSONUtil.getJSONStatus(status));
		pw.flush();
	}

}
