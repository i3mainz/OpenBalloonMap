package de.i3mainz.html;

import de.i3mainz.functions.PostGIS;
import de.i3mainz.functions.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SERVLET map
 *
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
@WebServlet(name = "map", urlPatterns = {"/map"})
public class map extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PostGIS gis;
		try {
			gis = new PostGIS();
			String bc = request.getParameter("ballooncode");
			String ev = request.getParameter("event");
			if ((bc == null || bc.isEmpty()) && (ev == null || ev.isEmpty())) {
				RequestDispatcher rd = request.getRequestDispatcher("map.jsp");
				rd.forward(request, response);
			} else if (bc != null) {
				ResultSet data = gis.getBalloonData(Utils.codeStringToInt(bc));
				if (data.next()) {
					String event = data.getString(1);
					String lat = data.getString(3);
					String lon = data.getString(2);
					RequestDispatcher rd = request.getRequestDispatcher("map.jsp?event=" + event + "&lat=" + lat + "&lon=" + lon + "&function=showBalloon&nr=" + Utils.codeStringToInt(bc));
					rd.forward(request, response);
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("map.jsp?function=noBalloon");
					rd.forward(request, response);
				}
			} else if (ev != null) {
				ResultSet event = gis.getEvent(ev);
				if (event.next()) {
					RequestDispatcher rd = request.getRequestDispatcher("map.jsp?event=" + ev + "&function=showEvent");
					rd.forward(request, response);
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("map.jsp?function=noEvent");
					rd.forward(request, response);
				}
			}
			gis.close();
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			response.resetBuffer();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("text/plain");
			e.printStackTrace(out);
			out.close();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
