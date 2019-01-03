package servlet;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/android/upload")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
 
		// 创建文件项目工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// 设置文件上传路径
		String upload = "/Users/haku/Desktop/Java/TinyCapsuleServer/res/";
		// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
		String temp = System.getProperty("java.io.tmpdir");
		// 设置缓冲区大小为 5M
		factory.setSizeThreshold(1024 * 1024 * 5);
		// 设置临时文件夹为temp
		factory.setRepository(new File(temp));
		// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
 
		// 解析结果放在List中
		try {
			List<FileItem> list = servletFileUpload.parseRequest(request);
			String filename = "";
			for (FileItem item : list) {
				String name = item.getFieldName();
				InputStream is = item.getInputStream();
 
				//System.out.println("the current name is " + name+" value:"+item.getString());
				
				if(name.equals("description")) {
					filename = item.getString().replace("/storage/emulated/0", "");
					System.out.println("filename = "+filename);
				}
				else if (name.equals("File")) {
					try {
						inputStream2File(is,
								upload + filename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} 
//				else {
//					String key = item.getName();
//					String value = item.getString();
//					System.out.println(key + "---" + value);
//				}
			}
 
			out.write("success");
		} catch (FileUploadException e) {
			e.printStackTrace();
			out.write("failure");
		}
 
		out.flush();
		out.close();
 
	}
 
	// 流转化成字符串
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
 
	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath)
			throws Exception {
		System.out.println("the file path is  :" + savePath);
		File file = new File(savePath);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		System.out.println(file.getAbsolutePath());
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();
	}

}
