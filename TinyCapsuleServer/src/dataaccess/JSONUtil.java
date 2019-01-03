package dataaccess;

import java.util.List;

import model.SimpleArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
	public static String getJSON(Object object) {
		return JSONObject.fromObject(object).toString();
    }
	public static String getJSON(List list) {
		return JSONObject.fromObject(new SimpleArray(list)).toString();
    }
	public static String getJSONwithStatus(Object object,int status) {
		return JSONObject.fromObject(object).toString();
    }
	public static String getJSONwithStatus(List list,int status) {
		return JSONArray.fromObject(list).toString();
    }
	public static String getJSONStatus(int status) {
		return "{\"status\":\""+status+"\"}";
	}
	
	public static Object getObject(String json) {
		return (Object)JSONObject.toBean(JSONObject.fromObject(json));
	}
}
