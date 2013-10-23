package com.tair_2_3.statmonitor.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tair_2_3.statmonitor.TairStatInfoReaderDeamon;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.etc.TairConstant;
import com.taobao.tair.json.JSONArray;
import com.taobao.tair.json.JSONObject;

/**
 * Servlet implementation class getrequest
 */
public class delrequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
	.getLog(delrequest.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public delrequest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = true;
		
		int Area = -1 ;
		String Key = null ;
		int Type = -1;
		String Result = null ;
		
		
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			log.info((String) o + ":"
					+ request.getParameter((String) o));
		}
		 
		 
		try {
			Area = Integer.parseInt(request.getParameter("Area"));
			Type = Integer.parseInt(request.getParameter("Type"));
			Key = Escape.unescape(request.getParameter("Key"));
		} catch (Exception e) {
			log.error(e.toString());
			result = false;
		}
		
		JSONObject res = new JSONObject();
		res.put("totalProperty", result ? 1 : 0);
		res.put("successProperty", result);
		JSONArray root = new JSONArray();
		if(result){
			ResultCode ret = null;
			try {
				switch (Type){
				case TairConstant.TAIR_STYPE_INT : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area, new Integer(Key));
					break;
				case TairConstant.TAIR_STYPE_STRING : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  Key); 
					break;
				case TairConstant.TAIR_STYPE_BOOL : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Boolean(Key)); 
					break;
				case TairConstant.TAIR_STYPE_LONG : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Long(Key)); 
					break;
				case TairConstant.TAIR_STYPE_DATE : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Date(Key)); 
					break;
				case TairConstant.TAIR_STYPE_BYTE : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Byte(Key)); 
					break;
				case TairConstant.TAIR_STYPE_FLOAT : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Float(Key)); 
					break;
				case TairConstant.TAIR_STYPE_DOUBLE : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  new Double(Key)); 
					break;
				case TairConstant.TAIR_STYPE_BYTEARRAY : 
					ret = TairStatInfoReaderDeamon.getTask().tm.delete(Area,  Key.getBytes()); 
					break;
				}
			} catch (NumberFormatException e) {
				log.error(e.toString());
				ret = null;
			}
			
			if (ret == null) {
				Result = "err occur while delete";
			} else {
				Result = ret.toString();
			}
			JSONObject foo = new JSONObject();
			foo.put("Area", Area);
			foo.put("Key", Key);
			foo.put("Type", Type);
			foo.put("Result", Result);
			root.add(foo);
			
		}
		res.put("root", root);
		String finalResult = res.toString();
		response.getOutputStream().println(new String(finalResult.getBytes("UTF-8"), "ISO-8859-1"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
