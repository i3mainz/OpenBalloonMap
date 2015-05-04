package de.i3mainz.functions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET Get
 *
 * @author Martin Unold M.Sc.
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "Get", urlPatterns = {"/Get.csv"})
public class Get extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/csv;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			out.println("id;event");
			//String name2 = Utils.toUTF8(request.getParameter("event"));
			String name = request.getParameter("event");
			PostGIS gis = new PostGIS();
			ResultSet events = gis.getEvent(name);
			if (events.next()) {
				ResultSet balloons = gis.getBalloons(events.getInt("id"));
				while (balloons.next()) {
					out.println(Utils.intToCodeString(balloons.getInt("nr")) + ";" + name);
				}
			}
			gis.close();
			response.setHeader("Content-Disposition", "attachment;filename=" + name + ".csv");
		} catch (Exception e) {
			response.resetBuffer();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("text/plain");
			e.printStackTrace(out);
		} finally {
			out.close();
		}
	}

}
