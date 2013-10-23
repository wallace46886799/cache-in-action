package com.tair_2_3.statmonitor.servlet;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Serializable;
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
import com.taobao.tair.etc.TairConstant;
import com.taobao.tair.json.JSONArray;
import com.taobao.tair.json.JSONObject;

/**
 * Servlet implementation class getrequest
 */
public class prefixgetrequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
	.getLog(prefixgetrequest.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public prefixgetrequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private Serializable doTranslate(String key, int type) {
    	Object o = null;
    	
		switch (type){
		case TairConstant.TAIR_STYPE_INT : 
			o = new Integer(key); 
			break;
		case TairConstant.TAIR_STYPE_STRING : 
			o = key; 
			break;
		case TairConstant.TAIR_STYPE_BOOL : 
			o = new Boolean(key); 
			break;
		case TairConstant.TAIR_STYPE_LONG : 
			o = new Long(key); 
			break;
		case TairConstant.TAIR_STYPE_DATE : 
			o  = new Date(key); 
			break;
		case TairConstant.TAIR_STYPE_BYTE : 
			o = new Byte(key); 
			break;
		case TairConstant.TAIR_STYPE_FLOAT : 
			o = new Float(key); 
			break;
		case TairConstant.TAIR_STYPE_DOUBLE : 
			o = new Double(key); 
			break;
		case TairConstant.TAIR_STYPE_BYTEARRAY : 
			o = key.getBytes(); 
			break;
		}
		
		return (Serializable) o;
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = true;
		
		int Area = -1 ;
		String Pkey = null, Skey = null;
		int PkeyType = -1, SkeyType = -1;
		String Value = null ;
		
		
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			log.info((String) o + ":"
					+ request.getParameter((String) o));
		}
		 
		try {
			Area = Integer.parseInt(request.getParameter("Area"));
			PkeyType = Integer.parseInt(request.getParameter("PkeyType"));
			Pkey = Escape.unescape(request.getParameter("Pkey"));
			SkeyType = Integer.parseInt(request.getParameter("SkeyType"));
			Skey = Escape.unescape(request.getParameter("Skey"));
		} catch (Exception e) {
			log.error(e.toString());
			result = false;
		}
		
		JSONObject res = new JSONObject();
		res.put("totalProperty", result?1:0);
		res.put("successProperty",result);
		JSONArray root = new JSONArray();
		if(result){
			Result<DataEntry> ret = null;
			try {
//				System.out.println(Pkey + "," + Skey);
				Serializable op = doTranslate(Pkey, PkeyType);
				Serializable os = doTranslate(Skey, SkeyType);
				ret = TairStatInfoReaderDeamon.getTask().tm.prefixGet(Area, op, os);
			} catch (NumberFormatException e) {
				log.error(e.toString());
				ret = null;
			}
			
			if (ret == null) {
				Value = "result is null";
			} else {
				Value = ret.toString();
			}
			JSONObject foo = new JSONObject();
			foo.put("Area", Area);
			foo.put("Pkey", Pkey);
			foo.put("PkeyType", PkeyType);
			foo.put("Skey", Skey);
			foo.put("SkeyType", SkeyType);
			foo.put("Value", Value);
			root.add(foo);
			
		} else {
			JSONObject foo = new JSONObject();
			foo.put("Area", request.getParameter("Area"));
			foo.put("Pkey", request.getParameter("Pkey"));
			foo.put("PkeyType", request.getParameter("PkeyType"));
			foo.put("Skey", request.getParameter("Skey"));
			foo.put("SkeyType", request.getParameter("SkeyType"));
			foo.put("Value", new String("invalid input, please check !!!!"));
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
