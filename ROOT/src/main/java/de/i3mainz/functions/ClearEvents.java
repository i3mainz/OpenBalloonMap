package de.i3mainz.functions;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET ClearEvents
 *
 * @author Martin Unold M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "ClearEvents", urlPatterns = {"/ClearEvents"})
public class ClearEvents extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html;charset=UTF-8");
			String name = Utils.toUTF8(request.getParameter("name"));
			PostGIS gis = new PostGIS();
			if (name.equals("all")) {
				gis.executeSQL("deleteEvents.sql");
				response.sendRedirect(Utils.REDIRECT_MAP);
			} else {
				if (gis.deleteEvent(name) > 0) {
					response.sendRedirect(Utils.REDIRECT_MAP);
				} else {
					gis.close();
					throw new Exception("Failed to execute ClearEvents");
				}
			}
			gis.close();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			PrintWriter out = response.getWriter();
			e.printStackTrace(out);
		}
	}

}
