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
import com.taobao.tair.comm.Transcoder;
import com.taobao.tair.etc.TairConstant;
import com.taobao.tair.etc.TairUtil;
import com.taobao.tair.impl.ConfigServer;
import com.taobao.tair.impl.DefaultTairManager;
import com.taobao.tair.json.JSONArray;
import com.taobao.tair.json.JSONObject;

/**
 * Servlet implementation class getrequest
 */
public class getdsrequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory
	.getLog(getdsrequest.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getdsrequest() {
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
		String DataServer = null ;
		
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			log.info((String) o + ":"
					+ request.getParameter((String) o));
		}
		 
		 
		try {
//			Area = Integer.parseInt(request.getParameter("Area"));
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
			DefaultTairManager dtm = (DefaultTairManager) TairStatInfoReaderDeamon.getTask().tm;
			ConfigServer cs = dtm.getConfigServer();
			Transcoder ts = dtm.getTranscoder();
			long serverIp = -1;
			
			try {
				switch (Type){
				case TairConstant.TAIR_STYPE_INT : 
					serverIp = cs.getServer(ts.encode(new Integer(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_STRING : 
					serverIp = cs.getServer(ts.encode(Key, false), true);
					break;
				case TairConstant.TAIR_STYPE_BOOL : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_LONG : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_DATE : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_BYTE : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_FLOAT : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_DOUBLE : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				case TairConstant.TAIR_STYPE_BYTEARRAY : 
					serverIp = cs.getServer(ts.encode(new Boolean(Key), false), true);
					break;
				}
			} catch (NumberFormatException e) {
				log.error(e.toString());
				serverIp = -1;
			}
			
			if (serverIp == -1) {
				DataServer = "err while find ";
			} else {
				DataServer = TairUtil.idToAddress(serverIp);
			}
			JSONObject foo = new JSONObject();
			foo.put("Key", Key);
			foo.put("DataServer", DataServer);
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
