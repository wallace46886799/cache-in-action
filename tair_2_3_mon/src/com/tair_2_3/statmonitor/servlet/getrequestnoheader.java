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
import com.taobao.tair.etc.TairConstant;
import com.taobao.tair.json.JSONArray;
import com.taobao.tair.json.JSONObject;

/**
 * Servlet implementation class getrequest
 */
public class getrequestnoheader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(getrequestnoheader.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getrequestnoheader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean result = true;

		int Area = -1;
		String Key = null;
		int Type = -1;
		String Value = null;

		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			Object o = e.nextElement();
			log.info((String) o + ":" + request.getParameter((String) o));
		}

		try {
			Area = Integer.parseInt(request.getParameter("Area"));
			Key = Escape.unescape(request.getParameter("Key"));
			log.debug("area is " + Area + ", Type is " + Type + ", Key is "
					+ Key);
		} catch (Exception e) {
			log.error(e.toString());
			result = false;
		}

		JSONObject res = new JSONObject();
		res.put("totalProperty", result ? 1 : 0);
		res.put("successProperty", result);
		JSONArray root = new JSONArray();
		if (result) {
			Result<DataEntry> ret = null;
			try {
				if (TairStatInfoReaderDeamon.getTask().tmNoHeader != null) {
					ret = TairStatInfoReaderDeamon.getTask().tmNoHeader.get(Area, Key);
					Value = ret.toString();
				} else {
					Value = "the monitor of this cluster doesn't support c++ query, please contace Â½Àë or ÌúÐÄ.";
				}
			} catch (NumberFormatException e) {
				log.error(e.toString());
				ret = null;
				Value = "result is null";
			}

			JSONObject foo = new JSONObject();
			foo.put("Area", Area);
			foo.put("Key", Key);
			foo.put("Value", Value);
			root.add(foo);

		} else {
			JSONObject foo = new JSONObject();
			foo.put("Area", request.getParameter("Area"));
			foo.put("Key", Escape.unescape(request.getParameter("Key")));
			foo.put("Value", new String("invalid input, please check !!!!"));
			root.add(foo);
		}
		res.put("root", root);
		String finalResult = res.toString();
		response.getOutputStream().println(
				new String(finalResult.getBytes("UTF-8"), "ISO-8859-1"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
