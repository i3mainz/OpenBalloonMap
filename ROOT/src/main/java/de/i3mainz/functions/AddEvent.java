package de.i3mainz.functions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET AddEvent
 *
 * @author Martin Unold M.Sc.
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "AddEvent", urlPatterns = {"/AddEvent"})
public class AddEvent extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html;charset=UTF-8");
			String name = Utils.toUTF8(request.getParameter("name"));
			String location = Utils.toUTF8(request.getParameter("location"));
			double lon = Utils.stringToDouble(request.getParameter("lon"));
			double lat = Utils.stringToDouble(request.getParameter("lat"));
			Timestamp timestamp = Utils.stringToTimestamp(request.getParameter("timestamp"));
			int balloons = Utils.stringToInt(request.getParameter("balloons"));
			if (balloons > 1000) {
				throw new Exception("too many: " + balloons);
			}
			PostGIS gis = new PostGIS();
			if (gis.insertEvent(name, location, lon, lat, timestamp, balloons) > 0) {
				response.sendRedirect("/Get.csv?event=" + name);
			} else {
				gis.close();
				throw new Exception("Failed to execute AddEvent");
			}
			gis.close();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			PrintWriter out = response.getWriter();
			e.printStackTrace(out);
		}
	}

}
