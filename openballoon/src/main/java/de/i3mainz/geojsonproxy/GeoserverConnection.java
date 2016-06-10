package de.i3mainz.geojsonproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * CLASS GeoserverConnection
 *
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
public class GeoserverConnection {

	/**
	 * Get all Findspots from Geoserver [Samian:findspot]
	 *
	 * @param arbeitsbereich
	 * @param name
	 * @return String [GeoJSON]
	 */
	public static String getAll(String arbeitsbereich, String name) {

		String geojson = "";

		String wfsurl = "http://localhost/geoserver/" + arbeitsbereich + "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=" + arbeitsbereich + ":" + name;
		wfsurl = wfsurl + "&outputFormat=application/json";

		try {

			URL url = new URL(wfsurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				geojson = geojson + line;
			}

			conn.disconnect();

		} catch (IOException ex) {
			geojson = ex.toString();
			System.out.println(ex);
		}

		return geojson;

	}

	/**
	 * Get all Findspots from Geoserver [Samian:findspot]
	 *
	 * @return String [GeoJSON]
	 */
	public static String getFilterLiteral(String arbeitsbereich, String name, String attr, String value) {

		String geojson = "";

		String wfsurl = "http://localhost/geoserver/" + arbeitsbereich + "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=" + arbeitsbereich + ":" + name;
		wfsurl = wfsurl + "&outputFormat=json";
		wfsurl = wfsurl + "&Filter=%3CFilter%3E%3CPropertyIsEqualTo%3E%3CPropertyName%3E" + attr + "%3C/PropertyName%3E%3CLiteral%3E" + value + "%3C/Literal%3E%3C/PropertyIsEqualTo%3E%3C/Filter%3E";
		wfsurl = wfsurl.replace(" ", "%20");

		try {

			URL url = new URL(wfsurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				geojson = geojson + line;
			}

			conn.disconnect();

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}

		return geojson;

	}

}
