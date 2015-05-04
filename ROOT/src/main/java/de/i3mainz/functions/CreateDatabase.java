package de.i3mainz.functions;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET CreateDatabase
 *
 * @author Martin Unold M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "CreateDatabase", urlPatterns = {"/CreateDatabase"})
public class CreateDatabase extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PostGIS gis = new PostGIS();
			if (!gis.executeSQL("create.sql")) {
				response.sendRedirect(Utils.REDIRECT_MAP);
			} else {
				gis.close();
				throw new Exception("Failed to execute create.sql");
			}
			gis.close();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			PrintWriter out = response.getWriter();
			e.printStackTrace(out);
		}
	}

}
