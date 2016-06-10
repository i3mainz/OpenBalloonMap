package de.i3mainz.functions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET AddFind
 *
 * @author Martin Unold M.Sc.
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "AddFind", urlPatterns = {"/AddFind"})
public class AddFind extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html;charset=UTF-8");
			int nr = Utils.codeStringToInt(request.getParameter("nr"));
			String location = Utils.toUTF8(request.getParameter("location"));
			double lon = Utils.stringToDouble(request.getParameter("lon"));
			double lat = Utils.stringToDouble(request.getParameter("lat"));
			Timestamp timestamp = Utils.stringToTimestamp(request.getParameter("timestamp") + " 00:00:00");
			String remark = Utils.toUTF8(request.getParameter("remark"));
			PostGIS gis = new PostGIS();
			int result = gis.insertFind(nr, location, lon, lat, timestamp, remark);
			RequestDispatcher rd = null;
			if (result > 0) // ok
			{
				response.sendRedirect("thx?ballooncode=" + request.getParameter("nr"));
			}
			if (result == 0) { // error
				gis.close();
				throw new Exception("Failed to execute AddFind");
			}
			if (result == -1) // if balloon number does not exist
			{
				response.sendRedirect("ballooncodeerror");
			}
			if (result == -2) // if balloon number is already used
			{
				response.sendRedirect("ballooncodeexists?ballooncode=" + request.getParameter("nr"));
			}
			gis.close();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			PrintWriter out = response.getWriter();
			e.printStackTrace(out);
		}
	}

}
