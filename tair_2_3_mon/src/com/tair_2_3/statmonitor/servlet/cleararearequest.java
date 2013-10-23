package com.tair_2_3.statmonitor.servlet;

import java.io.CharConversionException;
import java.io.IOException;
import java.net.URLDecoder;
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
public class cleararearequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
	.getLog(cleararearequest.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cleararearequest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = true;
		
		int Area = -1 ;
		String Passwd = null ;
		String Result = null ;
		
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			log.info((String) o + ":"
					+ request.getParameter((String) o));
		}
		 
		try {
			Area = Integer.parseInt(request.getParameter("Area"));
			Passwd = Escape.unescape(request.getParameter("Passwd"));
			log.debug("area is " + Area + ", Passwd is " + Passwd);
		} catch (Exception e) {
			log.error(e.toString());
			result = false;
		}
		
		JSONObject res = new JSONObject();
		res.put("totalProperty", result?1:0);
		res.put("successProperty",result);
		JSONArray root = new JSONArray();
		if(result){
			ResultCode ret = null;
			try {
				if (TairStatInfoReaderDeamon.getTask().tmRdb != null) {
					if (Passwd.equals(TairStatInfoReaderDeamon.getTask().passwd)) { 
						ret = TairStatInfoReaderDeamon.getTask().tmRdb.lazyRemoveArea(Area, "");
						Result = "the result of clear area " + Area + " is "+ ret.toString();
					} else {
						Result = "passwd error, the system has already record your ip for the sake of security...";
					}
				} else {
					Result = "this is not a rdb cluster, so can not use clear area";
				}
			} catch (NumberFormatException e) {
				log.error(e.toString());
				ret = null;
				Result = "result is null";
			}
			
			JSONObject foo = new JSONObject();
			foo.put("Area", Area);
			foo.put("Passwd", "");
			foo.put("Result", Result);
			root.add(foo);
			
		} else {
			JSONObject foo = new JSONObject();
			foo.put("Area", request.getParameter("Area"));
			foo.put("Key", "");
			foo.put("Result", new String("invalid input, please check !!!!"));
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
